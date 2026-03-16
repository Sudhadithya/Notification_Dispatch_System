package com.notificationsystem.api;

public class NotifyRequest {
    private String userId;
    private String eventType;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
}
