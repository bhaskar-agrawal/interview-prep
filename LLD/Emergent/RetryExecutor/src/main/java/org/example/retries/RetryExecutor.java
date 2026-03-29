package org.example;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class RetryExecutor {
    private final int maxRetries;
    private final long maxDelay;
    private final long baseDelay;

    public RetryExecutor(RetryConfig config) {
        this.maxRetries = config.maxRetries;
        this.maxDelay = config.maxDelay;
        this.baseDelay = config.baseDelay;
    }

    public <T> T handleTask(Callable<T> call) throws RetryExhaustedException {
        Exception lastException = null;
        for(int i=0; i<=maxRetries; i++) {
            try {
                return call.call();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RetryExhaustedException("interrupt", ex);
            }
            catch (Exception ex) {
                if(i<maxRetries) {
                    sleepWithBackoff(i);
                }
                lastException = ex;
            }
        }
        throw new RetryExhaustedException("failed to run", lastException);
    }

    private void sleepWithBackoff(int retry) throws RetryExhaustedException {
        long delayNow = baseDelay*(1L <<retry);
        long jitter = ThreadLocalRandom.current().nextLong(0, baseDelay);
        long realDelay = Math.min(delayNow+jitter, maxDelay);
        try {
            Thread.sleep(realDelay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RetryExhaustedException("interrupt", ex);
        }
    }
}
