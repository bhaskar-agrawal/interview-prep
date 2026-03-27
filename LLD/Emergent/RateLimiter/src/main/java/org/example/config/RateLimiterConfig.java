package org.example.config;

import org.example.entities.RateLimitAlgo;

public abstract class RateLimiterConfig {
    String endpoint;

    RateLimitAlgo algo;

    public RateLimiterConfig(String endpoint, RateLimitAlgo algo) {
        this.endpoint = endpoint;
        this.algo = algo;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public RateLimitAlgo getAlgo() {
        return this.algo;
    }
}
