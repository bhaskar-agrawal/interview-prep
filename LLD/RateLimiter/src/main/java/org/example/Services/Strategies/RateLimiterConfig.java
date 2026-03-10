package org.example.Services.Strategies;

import lombok.Data;
import org.example.Entities.RateLimiterAlgoType;

@Data
public class RateLimiterConfig {
    RateLimiterAlgoType type;
    String endpoint;
    int totalRequestsPerMinute;
    int maxBucketSize;
    int totalRequestsAddPerMinute;

    public RateLimiterConfig(RateLimiterAlgoType type, String endpoint, int totalReqPerMinute) {
        this.endpoint =endpoint;
        this.type = type;
        this.totalRequestsPerMinute = totalReqPerMinute;
        this.maxBucketSize =-1;
        this.totalRequestsAddPerMinute = -1;
    }

    public RateLimiterConfig(RateLimiterAlgoType type, String endpoint, int maxBucketSize, int totalRequestsAddPerMinute) {
        this.endpoint =endpoint;
        this.type = type;
        this.maxBucketSize = maxBucketSize;
        this.totalRequestsPerMinute =0;
        this.totalRequestsAddPerMinute = totalRequestsAddPerMinute;
    }
}
