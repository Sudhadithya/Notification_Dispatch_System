package com.notificationsystem.domain;

public class PushNotification extends Notification {

    public PushNotification(String userId, String message, String template, Priority priority) {
        super(userId, message, template, priority);
    }

    @Override
    public void send() {
        // Implementation for sending a push notification (e.g., via FCM/APNS).
        System.out.println("Executing Push Notification logic for User: " + getUserId() + " | Priority: " + getPriority() + " | Content: " + getMessage());
    }
}
