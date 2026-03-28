package org.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CircuitBreaker {
    Lock lock;
    Deque<Long> failedTimeStamps;
    long windowMs;
    long openTimeoutMs;
    int failureThreshold;
    volatile State state;
    volatile long gateOpenTimeStamp;
    public CircuitBreaker(int failureThreshold,
                          long windowMs,
                          long openTimeoutMs) {
        this.lock = new ReentrantLock();
        this.failureThreshold = failureThreshold;
        this.openTimeoutMs = openTimeoutMs;
        this.windowMs = windowMs;
        this.failedTimeStamps = new ArrayDeque<>();
        this.state = State.CLOSED;
        this.gateOpenTimeStamp = -1;
    }

    public <T> T execute(Callable<T> operation)
            throws Exception {
        long now = System.currentTimeMillis();
        if(this.state == State.OPEN || this.state == State.HALF_OPEN) {
            return handleOpenState(operation, now);
        }
        return handleClosedState(operation, now);
    }

    <T> T handleClosedState(Callable<T> operation, long nowTime) throws Exception {
        try {
            return operation.call();
        }
        catch (Exception ex) {
            if(isExceptionServerSide(ex)) {
                handleServerSideEx(nowTime);
            }
            throw ex;
        }
    }

    synchronized <T> T handleOpenState(Callable<T> operation, long nowTimeStamp) throws Exception {
        if(nowTimeStamp - this.gateOpenTimeStamp< this.openTimeoutMs) {
            throw new CircuitOpenException();
        }
        this.state = State.HALF_OPEN;
        try {
            T val = operation.call();
            transitToClosed();
            return val;
        }
        catch (Exception ex) {
            if(isExceptionServerSide(ex)) {
                transitToOpen(nowTimeStamp);
                throw new CircuitOpenException();
            }
            else {
               transitToClosed();
               throw ex;
            }
        }
    }

    void transitToClosed() {
        this.state = State.CLOSED;
        this.failedTimeStamps.clear();
        this.gateOpenTimeStamp =-1;
    }

    void transitToOpen(long nowTime) {
        this.state = State.OPEN;
        this.gateOpenTimeStamp = nowTime;
    }

    void handleServerSideEx(long now) {
        this.lock.lock();
        try {
            long remTimeStamp = now - windowMs;
            while(!this.failedTimeStamps.isEmpty() && this.failedTimeStamps.peekFirst()<remTimeStamp) {
                this.failedTimeStamps.pollFirst();
            }
            this.failedTimeStamps.add(now);
            if(this.failedTimeStamps.size()>=this.failureThreshold) {
                transitToOpen(now);
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    boolean isExceptionServerSide(Exception ex) {
        return !(ex instanceof IllegalArgumentException)
                && !(ex instanceof IllegalStateException)
                && !(ex instanceof NullPointerException);
    }
}
