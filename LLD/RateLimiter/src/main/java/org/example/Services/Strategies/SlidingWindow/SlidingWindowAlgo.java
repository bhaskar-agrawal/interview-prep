package org.example.Services.Strategies.SlidingWindow;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Services.Strategies.IRateLimiterAlgo;
import org.example.Services.Strategies.RateLimiterConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SlidingWindowAlgo implements IRateLimiterAlgo {
    int totalRequestsPerMinute;
    Queue<Long> requestsOfLastMin;
    long smallestTime;
    public SlidingWindowAlgo(RateLimiterConfig config) {
        this.totalRequestsPerMinute = config.getTotalRequestsPerMinute();
        this.requestsOfLastMin = new LinkedList<>();
    }

    public ApiResponse isRequestAllowed(ApiRequest request) {
        long now = System.currentTimeMillis();
        while(!requestsOfLastMin.isEmpty() && requestsOfLastMin.peek()<(now-60000)) {
            requestsOfLastMin.poll();
        }
        if(requestsOfLastMin.size()<totalRequestsPerMinute) {
            requestsOfLastMin.add(now);
            return ApiResponse.builder()
                    .isAllowed(true)
                    .retryAfterMillis(0)
                    .allowedRequestsLimit(totalRequestsPerMinute-requestsOfLastMin.size())
                    .endpoint(request.getEndpoint())
                    .clientId(request.getClientId())
                    .build();
        }
        else {
            long smallestTime = requestsOfLastMin.peek();
            long getNewMinTime = smallestTime+60000;
            return ApiResponse.builder()
                    .isAllowed(false)
                    .retryAfterMillis(getNewMinTime-now)
                    .allowedRequestsLimit(0)
                    .endpoint(request.getEndpoint())
                    .clientId(request.getClientId())
                    .build();
        }
    }
}
