package org.example.config;

import org.example.entities.RateLimitAlgo;

public class TokenBucketConfig extends RateLimiterConfig{

    int tokensPerMinute;
    int maxTokens;

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
        public Builder addEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder addTokensPerMinute(int tokensPerMinute) {
            this.tokensPerMinute = tokensPerMinute;
            return this;
        }

        public Builder addMaxBucketSize(int maxBucketSize) {
            this.maxBucketSize = maxBucketSize;
            return this;
        }

        public TokenBucketConfig build() {
            return new TokenBucketConfig(this);
        }
    }
}
