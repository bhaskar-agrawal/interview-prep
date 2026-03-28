package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        // Setup
        CircuitBreakerConfig config = new CircuitBreakerConfig("payment-service", 3, 5000L, 10000L);
        CircuitBreakerRegistry.getInstance().addCircuitBreaker(config);

// Simulate failures
        for(int i=0; i<3; i++) {
            try {
                CircuitBreakerRegistry.getInstance()
                        .handleRequest("payment-service", () -> { throw new RuntimeException("timeout"); });
                System.out.println("some passed");
            } catch(Exception ignored) {
                System.out.println("some passed with exception");
            }
        }

// This should throw CircuitBreakerOpenException
        try {
            CircuitBreakerRegistry.getInstance()
                    .handleRequest("payment-service", () -> "ok");
        }
        catch (CircuitOpenException ex) {
            System.out.println("this failed makes sense");
        }

    }
}