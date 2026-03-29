package org.example.provider;

import org.example.model.GatewayRequest;
import org.example.model.GatewayResponse;

public interface LLMProvider {
    GatewayResponse complete(GatewayRequest request);

    String getName();
}
