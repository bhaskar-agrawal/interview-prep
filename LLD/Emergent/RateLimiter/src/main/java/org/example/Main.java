package org.example;

import org.example.config.FixedWindowConfig;
import org.example.config.LeakyBucketConfig;
import org.example.config.SlidingWindowConfig;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        RateLimiterManager manager = RateLimiterManager.getInstance();
        FixedWindowConfig fixedWindowConfig= new FixedWindowConfig.Builder()
                .addEndpoint("search")
                        .setMaxRequestsPerMinute(15)
                                .build();
        SlidingWindowConfig slidingWindowConfig = new SlidingWindowConfig.Builder()
                .addEndpoint("buy")
                .addRequestsPerMinute(10)
                .build();

        LeakyBucketConfig leakyBucketConfig = new LeakyBucketConfig().
    }
}