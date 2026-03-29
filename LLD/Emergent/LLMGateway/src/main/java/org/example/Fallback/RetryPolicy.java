package org.example.Fallback;

public class RetryPolicy {
    private final int maxAttempts;       // 3
    private final long baseBackoffMs;    // 10ms
    private final double multiplier;     // e.g. 2.0 → 10, 20, 40ms
    private final double jitterFactor;

    public RetryPolicy(int maxAttempts, long baseBackoffMs) {
        this.maxAttempts = maxAttempts;
        this.baseBackoffMs = baseBackoffMs;
        this.multiplier = 2;
        this.jitterFactor = 0.2;
    }

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    public void waitForNextAttempt(int attempt) throws InterruptedException {
        System.out.println("attempt: "+ attempt);
        long waitMs = (long) (baseBackoffMs * Math.pow(multiplier, attempt));
        long jitter = (long) (waitMs* jitterFactor* Math.random());
        Thread.sleep(waitMs+jitter);
    }
}
