package com.notificationsystem.domain;

/**
 * Base abstract class representing a notification in the domain.
 * This class encapsulates common notification properties and enforces
 * the implementation of a specific sending mechanism through the abstract send() method.
 */
public abstract class Notification {
    private final String userId;
    private final String message;
    private final String template;
    private final Priority priority;

    public Notification(String userId, String message, String template, Priority priority) {
        this.userId = userId;
        this.message = message;
        this.template = template;
        this.priority = priority;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getTemplate() {
        return template;
    }

    public Priority getPriority() {
        return priority;
    }

    /**
     * Executes the domain logic required to actually send the notification.
     * This allows polymorphism when processing different types of notifications.
     */
    public abstract void send();
}
