package org.example.config;

import org.example.entities.RateLimitAlgo;

public class SlidingWindowConfig extends RateLimiterConfig{
    int requestsPerMinute;

    private SlidingWindowConfig(Builder builder) {
        super(builder.endpoint, RateLimitAlgo.SLIDING_WINDOW);
        this.requestsPerMinute = builder.requestsPerMinute;
    }

    public int getRequestsPerMinute() {
        return this.requestsPerMinute;
    }

    public static class Builder {
        String endpoint;
        int requestsPerMinute;
        public Builder addEndpoint(String endpoint) {
            this.endpoint= endpoint;
            return this;
        }

        public Builder addRequestsPerMinute(int requestsPerMinute) {
            this.requestsPerMinute = requestsPerMinute;
            return this;
        }

        public SlidingWindowConfig build() {
            return new SlidingWindowConfig(this);
        }
    }
}
