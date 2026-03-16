package com.notificationsystem.domain;

public class SMSNotification extends Notification {

    public SMSNotification(String userId, String message, String template, Priority priority) {
        super(userId, message, template, priority);
    }

    @Override
    public void send() {
        // Implementation for sending an SMS.
        System.out.println("Executing SMS logic for User: " + getUserId() + " | Priority: " + getPriority() + " | Content: " + getMessage());
    }
}
