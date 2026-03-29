package org.example;

public class RetryConfig {
    int maxRetries;
    long maxDelay;
    long baseDelay;

    public RetryConfig(int maxRetries, long maxDelay, long baseDelay) {
        this.maxRetries = maxRetries;
        this.maxDelay = maxDelay;
        this.baseDelay = baseDelay;
    }
}
