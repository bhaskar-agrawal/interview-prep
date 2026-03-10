package org.example.Services.Strategies.TokenBucket;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Services.Strategies.IRateLimiterAlgo;
import org.example.Services.Strategies.RateLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketAlgo implements IRateLimiterAlgo {
    private final int maxBuckets;
    private final int totalRequestsAddPerMinute;
    private final ConcurrentHashMap<String, ClientState> clientStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> clientLocks = new ConcurrentHashMap<>();

    private class ClientState {
        double allowedRequests;
        long lastFilledTime;

        ClientState() {
            this.allowedRequests = maxBuckets;
            this.lastFilledTime = System.currentTimeMillis();
        }
    }

    public TokenBucketAlgo(RateLimiterConfig config) {
        this.maxBuckets = config.getMaxBucketSize();
        this.totalRequestsAddPerMinute = config.getTotalRequestsAddPerMinute();
    }

    public ApiResponse isRequestAllowed(ApiRequest request) {
        String clientId = request.getClientId();
        ClientState state = clientStates.computeIfAbsent(clientId, k -> new ClientState());
        ReentrantLock lock = clientLocks.computeIfAbsent(clientId, k -> new ReentrantLock());

        lock.lock();
        try {
            long now = System.currentTimeMillis();
            double requestsToAdd = (double) ((now - state.lastFilledTime) * totalRequestsAddPerMinute) / (60 * 1000);
            state.allowedRequests = Math.min(state.allowedRequests + requestsToAdd, maxBuckets);
            state.lastFilledTime = now;

            if (state.allowedRequests >= 1) {
                state.allowedRequests -= 1;
                return ApiResponse.builder()
                        .isAllowed(true)
                        .retryAfterMillis(0)
                        .allowedRequestsLimit((int) state.allowedRequests)
                        .endpoint(request.getEndpoint())
                        .clientId(clientId)
                        .build();
            } else {
                long retryAfter = (long) ((1 - state.allowedRequests) * 60 * 1000 / totalRequestsAddPerMinute);
                return ApiResponse.builder()
                        .isAllowed(false)
                        .retryAfterMillis(retryAfter)
                        .allowedRequestsLimit(0)
                        .endpoint(request.getEndpoint())
                        .clientId(clientId)
                        .build();
            }
        } finally {
            lock.unlock();
        }
    }
}