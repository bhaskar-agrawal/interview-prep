package org.example.rateLimitingAlgos;

import org.example.config.FixedWindowConfig;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FixedWindowAlgo extends RateLimitAlgoBase{
    int maxRequestsPerMinute;
    long totalRequestsHandled;
    long lastMinute;
    Lock lock;

    public FixedWindowAlgo(FixedWindowConfig config) {
        this.maxRequestsPerMinute = config.getMaxRequestsPerMinute();
        this.totalRequestsHandled =0;
        this.lastMinute = System.currentTimeMillis();
        this.lock = new ReentrantLock();
    }

    public boolean allowRequest() {
        this.lock.lock();
        try {
            long currTime = System.currentTimeMillis();
            if(currTime-lastMinute>=60000) {
                this.lastMinute = currTime;
                this.totalRequestsHandled=0;
            }
            if(this.totalRequestsHandled<maxRequestsPerMinute) {
                this.totalRequestsHandled+=1;
                return true;
            }
            return false;
        }
        finally {
            this.lock.unlock();
        }
    }
}
