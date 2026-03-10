package org.example;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Entities.RateLimiterAlgoType;
import org.example.Services.Strategies.RateLimiterConfig;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<RateLimiterConfig> configList = new ArrayList<>();
        configList.add(new RateLimiterConfig(RateLimiterAlgoType.FIXED_WINDOW, "payment", 3));
        configList.add(new RateLimiterConfig(RateLimiterAlgoType.SLIDING_WINDOW, "login", 2));
        configList.add(new RateLimiterConfig(RateLimiterAlgoType.TOKEN_BUCKET, "pushMessage", 5, 2));

        APIHandler handler = new APIHandler(configList);

        System.out.println("=== Fixed Window: payment (limit=3) ===");
        for (int i = 1; i <= 5; i++) {
            ApiRequest req = new ApiRequest("client1", "payment");
            ApiResponse res = handler.isRequestAllowed(req);
            System.out.println("Request " + i + " -> allowed=" + res.isAllowed()
                    + ", remaining=" + res.getAllowedRequestsLimit()
                    + ", retryAfter=" + res.getRetryAfterMillis() + "ms");
        }

        System.out.println("\n=== Sliding Window: login (limit=2) ===");
        for (int i = 1; i <= 4; i++) {
            ApiRequest req = new ApiRequest("client2", "login");
            ApiResponse res = handler.isRequestAllowed(req);
            System.out.println("Request " + i + " -> allowed=" + res.isAllowed()
                    + ", remaining=" + res.getAllowedRequestsLimit()
                    + ", retryAfter=" + res.getRetryAfterMillis() + "ms");
        }

        System.out.println("\n=== Token bucket: pushMessage (limit=10) ===");
        for (int i = 1; i <= 50; i++) {
            ApiRequest req = new ApiRequest("client2", "pushMessage");
            ApiResponse res = handler.isRequestAllowed(req);
            System.out.println("Request " + i + " -> allowed=" + res.isAllowed()
                    + ", remaining=" + res.getAllowedRequestsLimit()
                    + ", retryAfter=" + res.getRetryAfterMillis() + "ms");
            if(i%30==0) {
                Thread.sleep(1000);
            }
            Thread.sleep(100);
        }

        System.out.println("\n=== Default algo: unknown endpoint ===");
        ApiRequest req = new ApiRequest("client3", "unknown");
        ApiResponse res = handler.isRequestAllowed(req);
        System.out.println("Request -> allowed=" + res.isAllowed()
                + ", remaining=" + res.getAllowedRequestsLimit());
    }
}