package org.example;

import org.example.config.*;
import org.example.entities.Request;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        testFixedWindow();
//        testSlidingWindow();
        testTokenBucket();
//        testLeakyBucket();
    }

    // Fires `total` concurrent requests and prints how many were allowed vs denied.
    static void runConcurrentTest(String label, String endpoint, int total) throws InterruptedException {
        RateLimiterManager manager = RateLimiterManager.getInstance();
        AtomicInteger allowed = new AtomicInteger();
        AtomicInteger denied = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(total);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (int i = 0; i < total; i++) {
            pool.submit(() -> {
                boolean result = manager.allowRequest(new Request(endpoint, "client-1"));
                if (result) allowed.incrementAndGet();
                else denied.incrementAndGet();
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown();
            });
        }

        latch.await();
        pool.shutdown();
        System.out.printf("[%s] allowed=%d  denied=%d  (total=%d)%n",
                label, allowed.get(), denied.get(), total);
    }

    static void testFixedWindow() throws InterruptedException {
        System.out.println("\n--- Fixed Window (limit=5/min, send 10) ---");
        RateLimiterManager manager = RateLimiterManager.getInstance();
        manager.addAlgoConfig(new FixedWindowConfig.Builder()
                .addEndpoint("fixed")
                .setMaxRequestsPerMinute(5)
                .build());
        runConcurrentTest("FixedWindow", "fixed", 10);
    }

    static void testSlidingWindow() throws InterruptedException {
        System.out.println("\n--- Sliding Window (limit=5/min, send 10) ---");
        RateLimiterManager manager = RateLimiterManager.getInstance();
        manager.addAlgoConfig(new SlidingWindowConfig.Builder()
                .addEndpoint("sliding")
                .addRequestsPerMinute(5)
                .build());
        runConcurrentTest("SlidingWindow", "sliding", 10);
    }

    static void testTokenBucket() throws InterruptedException {
        System.out.println("\n--- Token Bucket (max=5 tokens, 60/min refill, send 10) ---");
        RateLimiterManager manager = RateLimiterManager.getInstance();
        manager.addAlgoConfig(new TokenBucketConfig.Builder()
                .addEndpoint("token")
                .addMaxBucketSize(5)
                .addTokensPerMinute(60)
                .build());
        runConcurrentTest("TokenBucket", "token", 10);
    }

    static void testLeakyBucket() throws InterruptedException {
        System.out.println("\n--- Leaky Bucket (max=5 queue, 2 leaks/sec, send 10) ---");
        RateLimiterManager manager = RateLimiterManager.getInstance();
        manager.addAlgoConfig(new LeakyBucketConfig.Builder()
                .addEndpoint("leaky")
                .leaksPerSecond(2)
                .setMaxBucketSize(5)
                .build());

        // Send first burst: expect 5 allowed (bucket capacity), 5 denied
        runConcurrentTest("LeakyBucket burst-1", "leaky", 10);

        // Wait for bucket to drain (3 seconds = 6 leaks), then send another burst
        System.out.println("  [waiting 3s for bucket to drain...]");
        Thread.sleep(3000);
        runConcurrentTest("LeakyBucket burst-2", "leaky", 10);
    }
}