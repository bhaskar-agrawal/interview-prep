package org.example.model;

public class GatewayRequest {

    private final String model;
    private final String prompt;
    private final String apiKey;
    private final long timeoutMs;

    public GatewayRequest(String model, String prompt, String apiKey, long timeoutMs) {
        this.model = model;
        this.prompt = prompt;
        this.apiKey = apiKey;
        this.timeoutMs = timeoutMs;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getModel() {
        return this.model;
    }
}
