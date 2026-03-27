package org.example.rateLimitingAlgos;

import org.example.config.TokenBucketConfig;
import org.example.entities.Request;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketAlgo extends RateLimitAlgoBase{
    int maxBucketSize;
    double tokensPerMillis;
    Lock lock;
    int currentTokens;
    long lastFilledTimeInMillis;

    public TokenBucketAlgo(TokenBucketConfig config) {
        this.maxBucketSize = config.getMaxTokens();
        this.tokensPerMillis = config.getTokensPerMinute()/60000.0;
        this.lastFilledTimeInMillis = System.currentTimeMillis();
        this.currentTokens = maxBucketSize;

        this.lock = new ReentrantLock();
    }

    public boolean allowRequest() {
        this.lock.lock();
        try {
            long currTime = System.currentTimeMillis();
            long gapTime = currTime - lastFilledTimeInMillis;
            currentTokens = (int) Math.min(currentTokens+gapTime*tokensPerMillis, maxBucketSize);
            lastFilledTimeInMillis = currTime;
            if(currentTokens>0) {
                currentTokens-=1;
                return true;
            }
            return false;
        }
        finally {
            this.lock.unlock();
        }
    }
}
