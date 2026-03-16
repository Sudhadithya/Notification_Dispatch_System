package com.notificationsystem.infrastructure.channels;

import com.notificationsystem.domain.Notification;

/**
 * Interface representing a physical or logical channel through which notifications are sent.
 * Abstraction allows plugging in new channels (e.g., Slack) without modifying the dispatcher.
 */
public interface NotificationChannel {
    /**
     * Dispatches the given notification through this channel.
     * @param n the notification to send
     * @throws RuntimeException if sending fails, allowing retry mechanisms to catch it
     */
    void send(Notification n);

    /**
     * Checks if the channel is currently available to send notifications.
     * @return true if available, false otherwise
     */
    boolean isAvailable();
}
