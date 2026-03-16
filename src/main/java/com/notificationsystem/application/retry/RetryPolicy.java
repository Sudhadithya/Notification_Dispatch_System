package com.notificationsystem.application.retry;

/**
 * Strategy interface for executing tasks that may fail transiently.
 * Encapsulating retry logic away from the main dispatcher keeps the domain
 * code clean and focused.
 */
public interface RetryPolicy {
    /**
     * Executes the given task with retry logic.
     * @param task the runnable task to execute
     * @throws RuntimeException if the task fails after all retries
     */
    void execute(Runnable task);
}
