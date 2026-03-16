package com.notificationsystem.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class NotificationHistoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private String eventType;
    
    @Enumerated(EnumType.STRING)
    private ChannelType channel;
    
    private boolean success;
    private String errorMessage;
    private LocalDateTime timestamp;

    public NotificationHistoryEntry() {}

    public NotificationHistoryEntry(String userId, String eventType, ChannelType channel, boolean success, String errorMessage) {
        this.userId = userId;
        this.eventType = eventType;
        this.channel = channel;
        this.success = success;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getEventType() { return eventType; }
    public ChannelType getChannel() { return channel; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
