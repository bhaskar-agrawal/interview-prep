package org.example.Services.Strategies.FixedWindow;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Services.Strategies.IRateLimiterAlgo;
import org.example.Services.Strategies.RateLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class FixedWindowAlgo implements IRateLimiterAlgo {
    private final int countPerMinute;
    private final ConcurrentHashMap<String, ClientState> clientStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> clientLocks = new ConcurrentHashMap<>();

    private static class ClientState {
        int currCount;
        long currTime;

        ClientState(int countPerMinute) {
            this.currCount = countPerMinute;
            this.currTime = System.currentTimeMillis();
        }
    }

    public FixedWindowAlgo(RateLimiterConfig config) {
        this.countPerMinute = config.getTotalRequestsPerMinute();
    }

    public ApiResponse isRequestAllowed(ApiRequest request) {
        String clientId = request.getClientId();
        ClientState state = clientStates.computeIfAbsent(clientId, k -> new ClientState(countPerMinute));
        ReentrantLock lock = clientLocks.computeIfAbsent(clientId, k -> new ReentrantLock());

        lock.lock();
        try {
            long now = System.currentTimeMillis();
            long minuteDiff = (now - state.currTime) / 60000;
            if (minuteDiff >= 1) {
                state.currCount = countPerMinute;
                state.currTime = now;
            }
            if (state.currCount > 0) {
                state.currCount -= 1;
                return ApiResponse.builder()
                        .isAllowed(true)
                        .retryAfterMillis(0)
                        .allowedRequestsLimit(state.currCount)
                        .endpoint(request.getEndpoint())
                        .clientId(clientId)
                        .build();
            } else {
                return ApiResponse.builder()
                        .isAllowed(false)
                        .retryAfterMillis(60000 + state.currTime - now)
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