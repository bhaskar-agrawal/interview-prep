package org.example.provider;

import org.example.model.GatewayRequest;
import org.example.model.GatewayResponse;

public class MockProvider implements LLMProvider{
    private final String name;
    private final boolean shouldFail;
    private final int delayMs;

    public MockProvider(String name, boolean shouldFail, int delayMs) {
        this.name = name;
        this.shouldFail = shouldFail;
        this.delayMs = delayMs;
    }

    public String getName() {
        return this.name;
    }

    public GatewayResponse complete(GatewayRequest request) {
        try {
            Thread.sleep(this.delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if(this.shouldFail) {
            return new GatewayResponse("sample content", this.name, 800, 300, 200,503, "error due to internal issue");
        }
        else {
            return new GatewayResponse("sample content", this.name, 800, 1000, 300, 200);
        }
    }
}
