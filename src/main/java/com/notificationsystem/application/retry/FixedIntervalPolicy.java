package com.notificationsystem.application.retry;

public class FixedIntervalPolicy implements RetryPolicy {
    private final int maxRetries;
    private final long intervalMillis;

    public FixedIntervalPolicy(int maxRetries, long intervalMillis) {
        this.maxRetries = maxRetries;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void execute(Runnable task) {
        int attempts = 0;
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
                    Thread.sleep(intervalMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }
}
