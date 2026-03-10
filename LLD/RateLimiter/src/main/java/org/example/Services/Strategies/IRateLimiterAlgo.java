package org.example.Services.Strategies;

import com.sun.net.httpserver.Request;
import org.example.Entities.ApiRequest;
import org.example.Entities.ApiResponse;

public interface IRateLimiterAlgo {
    ApiResponse isRequestAllowed(ApiRequest request);
}
