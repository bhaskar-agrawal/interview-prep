package org.example.Services.Strategies.TokenBucket;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Services.Strategies.IRateLimiterAlgo;
import org.example.Services.Strategies.RateLimiterConfig;

public class TokenBucketAlgo implements IRateLimiterAlgo {
    int maxBuckets;
    int totalRequestsAddPerMinute;
    long lastFilledTime;
    double allowedRequests;
    public TokenBucketAlgo(RateLimiterConfig config) {
        this.maxBuckets = config.getMaxBucketSize();
        this.totalRequestsAddPerMinute = config.getTotalRequestsAddPerMinute();
        this.lastFilledTime = System.currentTimeMillis();
        this.allowedRequests = maxBuckets;
    }

    public ApiResponse isRequestAllowed(ApiRequest request) {
        long now = System.currentTimeMillis();
        double requestsToAdd = (double) ((now - this.lastFilledTime) * (totalRequestsAddPerMinute)) /(60*1000);
        allowedRequests+=requestsToAdd;
        allowedRequests = Math.min(allowedRequests, (double) this.maxBuckets);
        if(allowedRequests>1) {
            allowedRequests-=1;
            return ApiResponse.builder()
                    .isAllowed(true)
                    .retryAfterMillis(0)
                    .allowedRequestsLimit((int)allowedRequests)
                    .endpoint(request.getEndpoint())
                    .clientId(request.getClientId())
                    .build();
        }
        else {
            long getNewMinTime = (60*1000)/(totalRequestsAddPerMinute)+1;
            getNewMinTime-= (now-this.lastFilledTime);
            return ApiResponse.builder()
                    .isAllowed(false)
                    .retryAfterMillis(getNewMinTime)
                    .allowedRequestsLimit(0)
                    .endpoint(request.getEndpoint())
                    .clientId(request.getClientId())
                    .build();
        }
    }
}
