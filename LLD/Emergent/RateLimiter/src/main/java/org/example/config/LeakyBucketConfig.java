package org.example.config;

import org.example.entities.RateLimitAlgo;

import javax.swing.plaf.ButtonUI;

public class LeakyBucketConfig extends  RateLimiterConfig{
    int leaksPerSecond;
    int maxBucketSize;

    private LeakyBucketConfig(Builder builder) {
        super(builder.endpoint, RateLimitAlgo.LEAKY_BUCKET);
        this.leaksPerSecond = builder.leaksPerSecond;
        this.maxBucketSize = builder.maxBucketSize;
    }

    public int getLeaksPerSecond() {
        return this.leaksPerSecond;
    }

    public int getMaxBucketSize(){
        return this.maxBucketSize;
    }

    public static class Builder {
        int leaksPerSecond;
        int maxBucketSize;
        String endpoint;

        public Builder addEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder leaksPerMinute(int leaksPerSecond) {
            this.leaksPerSecond =leaksPerSecond;
            return this;
        }

        public Builder setMaxBucketSize(int maxBucketSize) {
            this.maxBucketSize = maxBucketSize;
            return this;
        }

        public LeakyBucketConfig build() {
            return new LeakyBucketConfig(this);
        }
    }
}
