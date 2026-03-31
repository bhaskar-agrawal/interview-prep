package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiterRegistry registry = RateLimiterRegistry.getInstance();
        registry.addRateLimiter("client1", 10, 3);
        registry.addRateLimiter("client2", 20, 5);

        ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i=0; i<100; i++) {
            service.submit(() -> {
                boolean firstClientAllowed = registry.tryAcquire("client1");
                //boolean secondClientAllowed = registry.tryAcquire("client2");

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                System.out.println("client1: "+ firstClientAllowed);
                //System.out.println("client2: "+ secondClientAllowed);
            });
        }
        service.shutdown();
        service.awaitTermination(100000, TimeUnit.MILLISECONDS);
    }
}