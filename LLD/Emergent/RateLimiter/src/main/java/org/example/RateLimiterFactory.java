package org.example;

import org.example.config.*;
import org.example.rateLimitingAlgos.*;

public class RateLimiterFactory {
    public static RateLimitAlgoBase create(RateLimiterConfig config) {
        switch (config.getAlgo()) {
            case TOKEN_BUCKET:
                return new TokenBucketAlgo((TokenBucketConfig) config);
            case LEAKY_BUCKET:
                return new LeakyBucketAlgo((LeakyBucketConfig) config);
            case FIXED_WINDOW:
                return new FixedWindowAlgo((FixedWindowConfig) config);
            case SLIDING_WINDOW:
                return new SlidingWindowAlgo((SlidingWindowConfig) config);
            default:
                throw new IllegalArgumentException("Unknown strategy: " + config.getAlgo());
        }
    }
}
