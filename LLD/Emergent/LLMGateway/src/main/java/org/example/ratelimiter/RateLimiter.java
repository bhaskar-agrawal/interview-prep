package org.example.ratelimiter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiter {
    private final int maxRequests;
    private final long windowMs;
    Map<String, Deque<Long>> apiKeyTimeStampsMap;
    Map<String, Lock> apiKeyLockMap;

    public RateLimiter(int maxRequests, long windowMs) {
        this.maxRequests = maxRequests;
        this.windowMs = windowMs;
        this.apiKeyLockMap = new ConcurrentHashMap<>();
        this.apiKeyTimeStampsMap = new ConcurrentHashMap<>();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this:: cleanup, 1, 1, TimeUnit.MINUTES);
    }

    public boolean tryAcquire(String apiKey) {
        Lock lock = this.apiKeyLockMap.computeIfAbsent(apiKey, k -> new ReentrantLock());
        lock.lock();
        try {
            long nowTimeStamp = System.currentTimeMillis();
            long toRem = nowTimeStamp- windowMs;
            Deque<Long> timeStamps = this.apiKeyTimeStampsMap.computeIfAbsent(apiKey, k-> new ArrayDeque<>());
            while(!timeStamps.isEmpty() && timeStamps.peekFirst()<toRem) {
                timeStamps.pollFirst();
            }
            if(timeStamps.size()<maxRequests) {
                timeStamps.add(nowTimeStamp);
                return true;
            }
            else {
                return false;
            }
        }
        finally {
            lock.unlock();
        }
    }

    public void cleanup() {
        List<String> toRem = new ArrayList<>();
        for(String key: this.apiKeyTimeStampsMap.keySet()) {
            Lock lock = this.apiKeyLockMap.get(key);
            if(lock==null) {
                continue;
            }
            lock.lock();
            try {
                if(this.apiKeyTimeStampsMap.getOrDefault(key, new ArrayDeque<>()).isEmpty()) {
                    this.apiKeyTimeStampsMap.remove(key);
                    apiKeyLockMap.remove(key);
                }
            }
            finally {
                lock.unlock();
            }
        }
    }
}
