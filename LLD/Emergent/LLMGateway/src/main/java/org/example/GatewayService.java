package org.example;

import org.example.Fallback.FallbackChain;
import org.example.model.GatewayRequest;
import org.example.model.GatewayResponse;
import org.example.provider.LLMProvider;
import org.example.provider.ProviderRegistry;
import org.example.ratelimiter.RateLimiter;

import java.util.List;

public class GatewayService {
    private final ProviderRegistry providerRegistry;
    private final RateLimiter rateLimiter;
    private final FallbackChain fallbackChain;

    public GatewayService(ProviderRegistry registry, RateLimiter limiter, FallbackChain chain) {
        this.providerRegistry = registry;
        this.rateLimiter = limiter;
        this.fallbackChain = chain;
    }

    public GatewayResponse complete(GatewayRequest request) {
        if(!rateLimiter.tryAcquire(request.getApiKey())) {
            return new GatewayResponse(null, null, 0,0,0, 429, "too many requets");
        }
        List<LLMProvider> providers = providerRegistry.getProviders(request.getModel());
        if(providers.isEmpty()) {
            return new GatewayResponse(null, null, 0,0,0, 404, "not found providers");
        }
        return fallbackChain.execute(request, providers);
    }
}
