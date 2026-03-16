package com.notificationsystem.domain;

public class EmailNotification extends Notification {

    public EmailNotification(String userId, String message, String template, Priority priority) {
        super(userId, message, template, priority);
    }

    @Override
    public void send() {
        // Implementation for sending an email.
        // In a real system, this would interact with an SMTP server or email API.
        System.out.println("Executing Email logic for User: " + getUserId() + " | Priority: " + getPriority() + " | Content: " + getMessage());
    }
}
