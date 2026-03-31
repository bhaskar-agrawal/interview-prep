package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiter {
    private double tokens;
    private long lastRefillTimeInMillis;
    private final long capacity;
    private final double refillRatePerMillis;
    private final Lock lock;

    public RateLimiter(long capacity, int refillRatePerSecond) {
        this.refillRatePerMillis = refillRatePerSecond/ 1000.0;
        this.capacity = capacity;
        this.lastRefillTimeInMillis = System.currentTimeMillis();
        this.tokens = capacity;
        this.lock = new ReentrantLock();
    }

    public boolean tryAcquire() {
        this.lock.lock();
        try {
            long now = System.currentTimeMillis();
            long delay = now- this.lastRefillTimeInMillis;
            this.lastRefillTimeInMillis = now;
            this.tokens = Math.min(this.tokens+ (delay*this.refillRatePerMillis), capacity);
            if(this.tokens>=1) {
                this.tokens-=1;
                return true;
            }
            else {
                return false;
            }
        }
        finally {
            this.lock.unlock();
        }
    }
}
