package org.example.rateLimitingAlgos;

import org.example.config.LeakyBucketConfig;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LeakyBucketAlgo extends RateLimitAlgoBase{
    int leaksPerSecond;
    int maxBucketSize;
    ArrayBlockingQueue<Integer> blockingQueue;
    ScheduledExecutorService service;
    boolean isShutDown;
    public LeakyBucketAlgo(LeakyBucketConfig config) {
        this.leaksPerSecond = config.getLeaksPerSecond();
        this.maxBucketSize = config.getMaxBucketSize();
        this.blockingQueue = new ArrayBlockingQueue<>(this.maxBucketSize);
        this.service = Executors.newSingleThreadScheduledExecutor();

        long intervalMillis = 1000/leaksPerSecond;
        this.isShutDown = false;
        this.service.schedule(this::leak, intervalMillis, TimeUnit.MILLISECONDS);

    }

    public boolean allowRequest() {
        return this.blockingQueue.offer(0);
    }

    public void leak() {
        Integer value = this.blockingQueue.poll();
    }

    public void shutDown() {
        this.service.shutdown();
    }
}
