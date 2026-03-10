package org.example.Entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse {
    boolean isAllowed;
    long retryAfterMillis;
    int allowedRequestsLimit;
    String clientId;
    String endpoint;
}

// retry after
// allowed or not.
// limit pending in the minute.