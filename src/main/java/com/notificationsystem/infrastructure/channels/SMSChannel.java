package com.notificationsystem.infrastructure.channels;

import com.notificationsystem.domain.Notification;

public class SMSChannel implements NotificationChannel {

    @Override
    public void send(Notification n) {
        if (!isAvailable()) {
            throw new RuntimeException("SMS channel is currently unavailable.");
        }
        n.send();
    }

    @Override
    public boolean isAvailable() {
        // Assume always available for this example
        return true;
    }
}
