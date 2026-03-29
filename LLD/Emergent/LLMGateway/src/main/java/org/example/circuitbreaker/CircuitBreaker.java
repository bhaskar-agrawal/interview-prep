package org.example.circuitbreaker;

import org.example.provider.ProviderRegistry;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class CircuitBreaker {
    private final AtomicReference<State> state;
    private Deque<Long> failureTimeStamps;
    private ReentrantLock lock;
    private volatile long openedAt;
    private int failureThreshold;
    private long windowMs;
    private long recoveryTimeMs;

    public CircuitBreaker(int failureThreshold, long windowMs, long recoveryMs) {
        this.failureThreshold = failureThreshold;
        this.windowMs = windowMs;
        this.recoveryTimeMs = recoveryMs;
        this.state = new AtomicReference<>(State.CLOSED);
        this.failureTimeStamps = new ArrayDeque<>();
        this.lock = new ReentrantLock();
    }

    public boolean isOpen() {
        if (state.get() == State.OPEN) {
            if (System.currentTimeMillis() - openedAt >= recoveryTimeMs) {
                if(this.state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    public void recordSuccess() {
        this.lock.lock();
        long now = System.currentTimeMillis();
        try {
            if(this.state.get() == State.HALF_OPEN) {
                transitStateToClosed();
            }
        }
        finally {
            lock.unlock();
        }
    }
    public void recordFailure() {
        this.lock.lock();
        try {
            long now = System.currentTimeMillis();
            long toRem = now - windowMs;
            if (this.state.get() == State.HALF_OPEN) {
                transitStateToOpen(now);
                return;
            }
            while(!this.failureTimeStamps.isEmpty() && this.failureTimeStamps.peekFirst()<toRem) {
                this.failureTimeStamps.pollFirst();
            }
            this.failureTimeStamps.add(now);
            if(this.failureTimeStamps.size()>=failureThreshold) {
                transitStateToOpen(now);
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    private void transitStateToOpen(long now) {
        this.state.set(State.OPEN);
        this.openedAt = now;
    }

    private void transitStateToClosed() {
        this.state.set(State.CLOSED);
        this.failureTimeStamps.clear();
    }

    enum State {
        OPEN,
        CLOSED,
        HALF_OPEN
    };

}
