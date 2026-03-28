package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CircuitBreakerRegistry {
    Map<String, CircuitBreaker> endpointBreakerMap;
    static CircuitBreakerRegistry instance;

    public static CircuitBreakerRegistry getInstance() {
        if(instance==null) {
            synchronized (CircuitBreakerRegistry.class) {
                if(instance==null) {
                    instance = new CircuitBreakerRegistry();
                }
            }
        }
        return instance;
    }

    private CircuitBreakerRegistry() {
        this.endpointBreakerMap = new ConcurrentHashMap<>();
    }

    public void addCircuitBreaker(CircuitBreakerConfig config) {
        this.endpointBreakerMap.compute(config.endpoint, (k,v)-> {
            return new CircuitBreaker(config.failureThreshold, config.windowMs, config.openTimeoutMs);
        });
    }

    public <T> T handleRequest(String endpoint, Callable<T> callable) throws Exception {
        CircuitBreaker breaker = this.endpointBreakerMap.get(endpoint);
        if(breaker!=null) {
            return breaker.execute(callable);
        }
        else {
            return callable.call();
        }
    }
}
