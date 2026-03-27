package org.example.rateLimitingAlgos;

import org.example.config.SlidingWindowConfig;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowAlgo extends RateLimitAlgoBase{
    int totalRequestsPerMinute;
    Deque<Long> requestsTimeStamps;
    Lock lock;

    public SlidingWindowAlgo(SlidingWindowConfig config) {
        this.totalRequestsPerMinute = config.getRequestsPerMinute();
        requestsTimeStamps = new ArrayDeque<>();
        this.lock = new ReentrantLock();
    }

    public boolean allowRequest() {
        this.lock.lock();
        try {
            long currTime = System.currentTimeMillis();
            long timeLessMinute = currTime - 60000;
            while(!this.requestsTimeStamps.isEmpty() && this.requestsTimeStamps.peekFirst()<=timeLessMinute) {
                this.requestsTimeStamps.pollFirst();
            }
            if(this.requestsTimeStamps.size()<totalRequestsPerMinute) {
                this.requestsTimeStamps.add(currTime);
                return true;
            }
            return false;
        }
        finally {
            this.lock.unlock();
        }
    }
}
