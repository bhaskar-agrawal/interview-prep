package org.example;

public class CircuitOpenException extends  Exception{
    public CircuitOpenException() {
        super("exception due to failed runs in circuit breaker");
    }
}
