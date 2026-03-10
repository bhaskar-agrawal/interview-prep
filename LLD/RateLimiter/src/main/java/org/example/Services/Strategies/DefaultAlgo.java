package org.example.Services.Strategies;

import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;

public class DefaultAlgo implements IRateLimiterAlgo{

    public ApiResponse isRequestAllowed(ApiRequest request) {
        return ApiResponse.builder()
                .isAllowed(true)
                .retryAfterMillis(0)
                .allowedRequestsLimit(1)
                .endpoint(request.getEndpoint())
                .clientId(request.getClientId())
                .build();
    }
}
