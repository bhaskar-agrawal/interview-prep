package org.example.config;

import org.example.entities.RateLimitAlgo;

public class TokenBucketConfig extends RateLimiterConfig{

    int tokensPerMinute;
    int maxTokens;
    public static Builder builder;
    private TokenBucketConfig(Builder builder) {
        super(builder.endpoint, RateLimitAlgo.TOKEN_BUCKET);
        this.tokensPerMinute = builder.tokensPerMinute;
        this.maxTokens = builder.maxBucketSize;
    }

    public int getTokensPerMinute() {
        return this.tokensPerMinute;
    }

    public  int getMaxTokens() {
        return this.maxTokens;
    }

    public static class Builder {
        String endpoint;
        int maxBucketSize;
        int tokensPerMinute;
        void addEndpoint(String endpoint) {
            this.endpoint= endpoint;
        }

        void addTokensPerMinute(int tokensPerMinute) {
            this.tokensPerMinute = tokensPerMinute;
        }

        void addMaxBucketSize(int maxBucketSize) {
            this.maxBucketSize = maxBucketSize;
        }

        TokenBucketConfig build() {
            return new TokenBucketConfig(this);
        }
    }
}
