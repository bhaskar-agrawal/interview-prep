package org.example.Services.Strategies;

import org.example.Entities.RateLimiterAlgoType;
import org.example.Services.Strategies.FixedWindow.FixedWindowAlgo;
import org.example.Services.Strategies.SlidingWindow.SlidingWindowAlgo;
import org.example.Services.Strategies.TokenBucket.TokenBucketAlgo;

public class RateLimiterAlgoFactory {

    public IRateLimiterAlgo getObject(RateLimiterConfig config) {
        RateLimiterAlgoType type = config.type;
        if(type==RateLimiterAlgoType.FIXED_WINDOW) {
            return new FixedWindowAlgo(config);
        }
        if(type == RateLimiterAlgoType.SLIDING_WINDOW) {
            return new SlidingWindowAlgo( config);
        }
        if(type==RateLimiterAlgoType.TOKEN_BUCKET) {
            return new TokenBucketAlgo(config);
        }
        return new DefaultAlgo();
    }
}
