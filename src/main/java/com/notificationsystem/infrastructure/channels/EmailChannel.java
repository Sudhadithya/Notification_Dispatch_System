package com.notificationsystem.infrastructure.channels;

import com.notificationsystem.domain.Notification;

public class EmailChannel implements NotificationChannel {

    @Override
    public void send(Notification n) {
        if (!isAvailable()) {
            throw new RuntimeException("Email channel is currently unavailable.");
        }
        // Delegate to the notification's specific sending logic
        n.send();
    }

    @Override
    public boolean isAvailable() {
        // Assume always available for this example
        return true;
    }
}
