package org.example;

import org.example.config.RateLimiterConfig;
import org.example.entities.Request;
import org.example.rateLimitingAlgos.RateLimitAlgoBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterManager {
    Map<String, RateLimitAlgoBase> endpointAlgoMap;
    static volatile RateLimiterManager instance=null;

    public static RateLimiterManager getInstance() {
        if(instance==null) {
            synchronized (RateLimiterManager.class) {
                if(instance==null) {
                    instance = new RateLimiterManager();
                }
            }
        }
        return instance;
    }

    private RateLimiterManager() {
        this.endpointAlgoMap = new ConcurrentHashMap<>();
    }

    public void addAlgoConfig(RateLimiterConfig config) {
        this.endpointAlgoMap.compute(config.getEndpoint(), (k,v)-> v==null? RateLimiterFactory.create(config): v);
    }

    public boolean allowRequest(Request request) {
        String endpoint = request.getEndpoint();
        if(this.endpointAlgoMap.containsKey(endpoint)) {
            return this.endpointAlgoMap.get(endpoint).allowRequest();
        }
        else {
            return true;
        }
    }
}
