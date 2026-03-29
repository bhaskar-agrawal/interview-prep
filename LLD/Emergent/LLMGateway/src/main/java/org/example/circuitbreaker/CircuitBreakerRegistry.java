package org.example.circuitbreaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CircuitBreakerRegistry {
    Map<String, CircuitBreaker> providerNameCircuitBreakerMap;
    private final int failureThreshold;
    private final long windowMs;
    private final long recoveryMs;

    public CircuitBreakerRegistry(int failureThreshold, long windowMs, long recoveryMs) {
        this.providerNameCircuitBreakerMap = new ConcurrentHashMap<>();
        this.failureThreshold = failureThreshold;
        this.windowMs = windowMs;
        this.recoveryMs = recoveryMs;
    }

    public CircuitBreaker getOrCreate(String providerName) {
        return this.providerNameCircuitBreakerMap.compute(providerName, (k,v)-> {
            if(v==null) {
                v= new CircuitBreaker(this.failureThreshold, this.windowMs, this.recoveryMs);
            }
            return v;
        });
    }
}
