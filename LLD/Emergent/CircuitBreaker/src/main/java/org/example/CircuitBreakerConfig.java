package org.example;

public class CircuitBreakerConfig {
    String endpoint;
    int failureThreshold;
    long windowMs;
    long openTimeoutMs;

    public CircuitBreakerConfig(String endpoint, int failureThreshold, long windowMs, long openTimeoutMs) {
        this.endpoint = endpoint;
        this.failureThreshold = failureThreshold;
        this.windowMs = windowMs;
        this.openTimeoutMs = openTimeoutMs;
    }
}
