package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

public class FallbackChain <T>{

    private List<Callable<T>> callables;

    private FallbackChain(builder<T> builder) {
        this.callables = new ArrayList<>();
        this.callables.addAll(builder.callables);
    }

    public T execute() throws FallbackFailedException {
        List<Exception> exceptions = new ArrayList<>();
        for(Callable<T> callable: this.callables) {
            try {
                return callable.call();
            }
            catch (Exception exception) {
                exceptions.add(exception);
            }
        }
        throw new FallbackFailedException(exceptions);
    }

    public static class builder <T>{
        private final List<Callable<T>> callables;

        public builder() {
            callables = new ArrayList<>();
        }

        public builder<T> addFallback(Callable<T> callable) {
            this.callables.add(callable);
            return this;
        }

        public FallbackChain<T> build() {
            return new FallbackChain<T>(this);
        }
    }
}
