
time: 7:25 start

RateLimiterConfig
SlidingWindowCOnfig
FixedWindowConfig
TokenBucketConfig
LeakyBucketConfig

RateLimiterAlgo 
{
  + allowRequest(Request request): boolean
}
SlidingWindowAlgo
FixedWindowAlgo
TokenBucket
LeakyBucketAlgo

RateLimiterManager 
{
  - Map<String, RateLimiterAlgo> endpointAlgoMapping;
  - 
}

Request {
  String endpoint;
  String clientId;
}

Main: demo class, multiple threads touching multiple classes
design: bottom to top. 
