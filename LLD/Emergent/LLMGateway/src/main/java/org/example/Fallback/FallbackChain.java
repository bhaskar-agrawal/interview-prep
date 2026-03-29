package org.example.Fallback;

import org.example.circuitbreaker.CircuitBreaker;
import org.example.circuitbreaker.CircuitBreakerRegistry;
import org.example.model.GatewayRequest;
import org.example.model.GatewayResponse;
import org.example.model.ProviderConfig;
import org.example.provider.LLMProvider;
import org.example.provider.ProviderRegistry;

import java.util.List;

public class FallbackChain {
    private final RetryPolicy retryPolicy;
    private final CircuitBreakerRegistry breakerRegistry;

    public FallbackChain(RetryPolicy policy, CircuitBreakerRegistry registry) {
        this.retryPolicy = policy;
        this.breakerRegistry = registry;
    }

    public GatewayResponse execute(GatewayRequest request, List<LLMProvider> providers) {
        for(LLMProvider provider: providers) {
            System.out.println("working with: "+ provider.getName());
            CircuitBreaker circuitBreaker = this.breakerRegistry.getOrCreate(provider.getName());
            for(int i=0; i<this.retryPolicy.getMaxAttempts(); i++) {
                if(circuitBreaker.isOpen()) {
                    System.out.println("open system");
                    break;
                }
                try {
                    GatewayResponse response= provider.complete(request);
                    if(response.getStatusCode()>=500) {
                        circuitBreaker.recordFailure();
                        if(i<this.retryPolicy.getMaxAttempts()-1) {
                            retryPolicy.waitForNextAttempt(i);
                        }
                        continue;
                    }
                    else if(response.getStatusCode()>=400) {
                        return response;
                    }
                    else {
                        circuitBreaker.recordSuccess();
                        return response;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new GatewayResponse(null, null, 0, 0, 0, 503, "interrupted");
                }
                catch (Exception ex) {
                    System.out.println("excpetion, just continuing");
                }
            }
        }
        return new GatewayResponse(null, null, 0,0,0, 503, "all providers exhausted");
    }
}
