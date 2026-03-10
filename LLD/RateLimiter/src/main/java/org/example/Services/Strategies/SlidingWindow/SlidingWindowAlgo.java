package org.example.Services.Strategies.SlidingWindow;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Services.Strategies.IRateLimiterAlgo;
import org.example.Services.Strategies.RateLimiterConfig;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowAlgo implements IRateLimiterAlgo {
    private final int totalRequestsPerMinute;
    private final ConcurrentHashMap<String, ClientState> clientStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> clientLocks = new ConcurrentHashMap<>();

    private static class ClientState {
        Queue<Long> requestsOfLastMin = new LinkedList<>();
    }

    public SlidingWindowAlgo(RateLimiterConfig config) {
        this.totalRequestsPerMinute = config.getTotalRequestsPerMinute();
    }

    public ApiResponse isRequestAllowed(ApiRequest request) {
        String clientId = request.getClientId();
        ClientState state = clientStates.computeIfAbsent(clientId, k -> new ClientState());
        ReentrantLock lock = clientLocks.computeIfAbsent(clientId, k -> new ReentrantLock());

        lock.lock();
        try {
            long now = System.currentTimeMillis();
            while (!state.requestsOfLastMin.isEmpty() && state.requestsOfLastMin.peek() < (now - 60000)) {
                state.requestsOfLastMin.poll();
            }
            if (state.requestsOfLastMin.size() < totalRequestsPerMinute) {
                state.requestsOfLastMin.add(now);
                return ApiResponse.builder()
                        .isAllowed(true)
                        .retryAfterMillis(0)
                        .allowedRequestsLimit(totalRequestsPerMinute - state.requestsOfLastMin.size())
                        .endpoint(request.getEndpoint())
                        .clientId(clientId)
                        .build();
            } else {
                long smallestTime = state.requestsOfLastMin.peek();
                long retryAfter = smallestTime + 60000 - now;
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