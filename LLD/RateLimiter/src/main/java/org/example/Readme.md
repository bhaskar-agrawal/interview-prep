Rate limiter LLD:

Requirements, entities, class design, implementation, verification

requirements:
1. rate limit the client, on the basis of config
2. config: endpoint, algorithm, algoConfig: capacity, refillratepersecond. This will differ wrt algorithm also
3. support multiple rate limit algo: token bucket, sliding window, fixed window, leaky bucket
4. rate limit the clients. 
5. By default: allow all requests to pass.

entities:
- Request -> clientId, endpointName
- RateLimitingConfig (different for different algos)
- RateLimiterFactory- create different rate limiter algo depending on the parameters in config.
- TokenBucketAlgo
- LeakyBucketAlgo
- SlidingWindowAlgo
- FixedWindowAlgo
- ApiHandler

class Design:

Request {
 - clientId
 - endpointName
}

ApiHandler {
  - Map<String, RateLimitAlgoObject> endpointRateLimitMapping
  + ApiHandler(List<RateLimiterConfig> configList)
  + isRequestAllowed(Request request): boolean
}

abstract RateLimiterConfig {
  - endpoint
  - algorithm
}

enum ratelimitAlgoType {
  TOKEN_BUCKET, LEAKY_BUCKET, SLIDING_WINDOW, FIXED_WINDOW
}

RateLimiterFactory {
  + createRateLimiterObject(enum ratelimitAlgoType, RateLimiterConfig config)
}

IRateLimiterAlgo {
  + allowRequest(Request request)
}

TokenBucketAlgo implements IRateLimiterAlgo
LeakyBucketAlgo implements IRateLimiterAlgo
SlidingWindowAlgo implements IRateLimiterAlgo
FixedWindowAlgo implements IRateLimiterAlgo

Implementation:

