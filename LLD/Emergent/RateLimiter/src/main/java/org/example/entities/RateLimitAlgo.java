package org.example.entities;

public enum RateLimitAlgo {
    SLIDING_WINDOW,
    FIXED_WINDOW,
    TOKEN_BUCKET,
    LEAKY_BUCKET
}
