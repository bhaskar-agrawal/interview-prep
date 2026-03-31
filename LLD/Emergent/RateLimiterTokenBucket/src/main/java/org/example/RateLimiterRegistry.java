package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterRegistry {

    private Map<String, RateLimiter> clientIdRateLimiterMap;
    private static RateLimiterRegistry instance;

    private RateLimiterRegistry() {
        this.clientIdRateLimiterMap = new ConcurrentHashMap<>();
    }

    public static RateLimiterRegistry getInstance() {
        if(instance ==null) {
            synchronized (RateLimiterRegistry.class) {
                if(instance ==null) {
                    instance = new RateLimiterRegistry();
                }
            }
        }
        return instance;
    }

    public void addRateLimiter(String clientId, long capacity, int refillRatePerSecond) {
        this.clientIdRateLimiterMap.computeIfAbsent(clientId, k-> new RateLimiter(capacity, refillRatePerSecond));
    }

    public boolean tryAcquire(String clientId) {
        RateLimiter rateLimiter = this.clientIdRateLimiterMap.get(clientId);
        if(rateLimiter==null) {
            System.out.println("code where null");
            return true;
        }
        else {
            //System.out.println("ratelimiter found");
            return rateLimiter.tryAcquire();
        }
    }
}
