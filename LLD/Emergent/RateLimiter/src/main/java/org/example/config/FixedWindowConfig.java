package org.example.config;

import org.example.entities.RateLimitAlgo;

public class FixedWindowConfig extends RateLimiterConfig{
    int maxRequestsPerMinute;

    private FixedWindowConfig(Builder builder) {
        super(builder.endpoint, RateLimitAlgo.FIXED_WINDOW);
        this.maxRequestsPerMinute = builder.maxRequestsPerMinute;
    }

    public int getMaxRequestsPerMinute() {
        return this.maxRequestsPerMinute;
    }

    public static class Builder {
        int maxRequestsPerMinute;
        String endpoint;

        public Builder setMaxRequestsPerMinute(int maxRequestsPerMinute) {
            this.maxRequestsPerMinute= maxRequestsPerMinute;
            return this;
        }

        public Builder addEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public FixedWindowConfig build() {
            return new FixedWindowConfig(this);
        }
    }
}
