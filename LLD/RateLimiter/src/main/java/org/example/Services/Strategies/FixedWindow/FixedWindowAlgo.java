package org.example.Services.Strategies.FixedWindow;

import com.sun.net.httpserver.Request;
import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Services.Strategies.IRateLimiterAlgo;
import org.example.Services.Strategies.RateLimiterConfig;

public class FixedWindowAlgo implements IRateLimiterAlgo {
    int countPerMinute;
    long currTime;
    int currCount;

    public FixedWindowAlgo(RateLimiterConfig config) {
        this.countPerMinute = config.getTotalRequestsPerMinute();
        currTime = System.currentTimeMillis();
        this.currCount = countPerMinute;
    }

    public ApiResponse isRequestAllowed(ApiRequest request) {
        long now = System.currentTimeMillis();
        long minuteDiff = (now-currTime)/60000;
        if(minuteDiff>=1) {
            currCount = countPerMinute;
            currCount-=1;
            currTime = now;
            return ApiResponse.builder()
                    .isAllowed(true)
                    .retryAfterMillis(0)
                    .allowedRequestsLimit(currCount)
                    .endpoint(request.getEndpoint())
                    .clientId(request.getClientId())
                    .build();
        }
        else {
            if(currCount>0) {
                currCount-=1;
                return ApiResponse.builder()
                        .isAllowed(true)
                        .retryAfterMillis(0)
                        .allowedRequestsLimit(currCount)
                        .endpoint(request.getEndpoint())
                        .clientId(request.getClientId())
                        .build();
            }
            else {
                return ApiResponse.builder()
                        .isAllowed(false)
                        .retryAfterMillis(60000+currTime-now)
                        .allowedRequestsLimit(0)
                        .endpoint(request.getEndpoint())
                        .clientId(request.getClientId())
                        .build();
            }
        }
    }
}
