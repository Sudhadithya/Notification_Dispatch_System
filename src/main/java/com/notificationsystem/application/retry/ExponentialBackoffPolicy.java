package com.notificationsystem.application.retry;

public class ExponentialBackoffPolicy implements RetryPolicy {
    private final int maxRetries;
    private final long baseIntervalMillis;

    public ExponentialBackoffPolicy(int maxRetries, long baseIntervalMillis) {
        this.maxRetries = maxRetries;
        this.baseIntervalMillis = baseIntervalMillis;
    }

    @Override
    public void execute(Runnable task) {
        int attempts = 0;
        long currentInterval = baseIntervalMillis;

        while (attempts < maxRetries) {
            try {
                task.run();
                return; // Success
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw new RuntimeException("Task failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(currentInterval);
                    currentInterval *= 2; // Exponential backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }
}
