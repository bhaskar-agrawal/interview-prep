package org.example;

import org.example.Fallback.FallbackChain;
import org.example.Fallback.RetryPolicy;
import org.example.circuitbreaker.CircuitBreakerRegistry;
import org.example.model.GatewayRequest;
import org.example.model.GatewayResponse;
import org.example.provider.LLMProvider;
import org.example.provider.MockProvider;
import org.example.provider.ProviderRegistry;
import org.example.ratelimiter.RateLimiter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        CircuitBreakerRegistry cbRegistry = new CircuitBreakerRegistry(3, 10000, 5000);
        RetryPolicy retryPolicy = new RetryPolicy(3, 10);
        FallbackChain fallbackChain = new FallbackChain(retryPolicy, cbRegistry);
        RateLimiter rateLimiter = new RateLimiter(5, 60000);
        ProviderRegistry providerRegistry = new ProviderRegistry();

        LLMProvider trueProvider = new MockProvider("bhaskar", true, 10);
        LLMProvider falseProvider = new MockProvider("murli", false, 10);
        providerRegistry.register("gpt-40", trueProvider);
        providerRegistry.register("gpt-40", falseProvider);

        GatewayService service = new GatewayService(providerRegistry, rateLimiter, fallbackChain);
//        GatewayResponse response = service.complete(new GatewayRequest("gpt-40", "bhaskar", "123", 100));
//        System.out.println(response.getStatusCode());
//
//        GatewayResponse response = service.complete(new GatewayRequest("gpt-40", "bhaskar", "123", 100));
//        System.out.println(response.getStatusCode());
        for(int i=0; i<6; i++) {
            GatewayResponse response = service.complete(new GatewayRequest("gpt-40", "bhaskar", "123", 100));
            System.out.println(response.getStatusCode());
            Thread.sleep(8000);
        }

        // 3. Create GatewayService
        // 4. Make a request and print response

        // 5. Test rate limiting — make 6 requests, assert 6th returns 429

    }
}