package org.example;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;
import org.example.Services.Strategies.DefaultAlgo;
import org.example.Services.Strategies.IRateLimiterAlgo;
import org.example.Services.Strategies.RateLimiterAlgoFactory;
import org.example.Services.Strategies.RateLimiterConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIHandler {
    Map<String, IRateLimiterAlgo> endpointRateLimiterMapping;
    RateLimiterAlgoFactory factory;
    IRateLimiterAlgo defaultAlgo;

    public APIHandler(List<RateLimiterConfig> configList) {
        endpointRateLimiterMapping = new HashMap<>();
        factory = new RateLimiterAlgoFactory();
        defaultAlgo = new DefaultAlgo();
        createMapping(configList);
    }

    public ApiResponse isRequestAllowed(ApiRequest request) {
        String endpoint = request.getEndpoint();
        if(endpointRateLimiterMapping.containsKey(endpoint)) {
            return endpointRateLimiterMapping.get(endpoint).isRequestAllowed(request);
        }
        else {
            return defaultAlgo.isRequestAllowed(request);
        }
    }

    private void createMapping(List<RateLimiterConfig> configList) {
        for(RateLimiterConfig config: configList) {
            endpointRateLimiterMapping.put(config.getEndpoint(), factory.getObject(config));
        }
    }
}


//ApiHandler {
//    - Map<String, RateLimitAlgoObject> endpointRateLimitMapping
//            + ApiHandler(List<RateLimiterConfig> configList)
//            + isRequestAllowed(Request request): boolean
//}