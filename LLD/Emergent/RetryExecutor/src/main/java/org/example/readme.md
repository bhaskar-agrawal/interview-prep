
RetryExecutor {
   + RetryExecutor(RetryConfig config)
   + handleTask(Callable<T> fun)
   - handleException(int retryCount)
}

RetryConfig {
  maxRetries
  baseDelay
  maxDelayMs
}

