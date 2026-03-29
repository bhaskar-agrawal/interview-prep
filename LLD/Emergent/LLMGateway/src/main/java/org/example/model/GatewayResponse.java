package org.example.model;

public class GatewayResponse {
    private final String content;
    private final String providerName;

    private final int inputTokens;
    private final int outputTokens;
    private final long latencyMs;
    private final int statusCode;

    private final String errorMessage;

    public GatewayResponse(String content, String providerName, int inputTokens, int outputTokens, long latencyMs, int statusCode) {
        this.content = content;
        this.providerName = providerName;
        this.inputTokens = inputTokens;
        this.outputTokens = outputTokens;
        this.latencyMs = latencyMs;
        this.statusCode = statusCode;
        this.errorMessage = "";
    }

    public GatewayResponse(String content, String providerName, int inputTokens, int outputTokens, long latencyMs, int statusCode, String errorMessage) {
        this.content = content;
        this.providerName = providerName;
        this.inputTokens = inputTokens;
        this.outputTokens = outputTokens;
        this.latencyMs = latencyMs;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
