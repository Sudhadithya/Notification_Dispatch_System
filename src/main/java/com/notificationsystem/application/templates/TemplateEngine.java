package com.notificationsystem.application.templates;

/**
 * Responsible for generating the message content for a notification.
 * This separates the concern of content creation from the notification delivery mechanism.
 */
public class TemplateEngine {

    /**
     * Resolves the event type into a human-readable message.
     * @param eventType the raw event identifier
     * @return the formatted message string
     */
    public String generateMessage(String eventType) {
        // In a real application, this would load templates from a database or file system
        // and perform variable interpolation.
        return switch (eventType.toUpperCase()) {
            case "SYNC_FAILED" -> "Your recent data synchronization has failed. Please check your connection and try again.";
            case "SYNC_SUCCESS" -> "Data synchronization completed successfully.";
            case "PASSWORD_RESET" -> "A password reset was requested for your account.";
            default -> "You have a new notification regarding: " + eventType;
        };
    }
}
