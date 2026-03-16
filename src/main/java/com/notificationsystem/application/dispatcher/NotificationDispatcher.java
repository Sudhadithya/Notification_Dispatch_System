package com.notificationsystem.application.dispatcher;

import com.notificationsystem.infrastructure.channels.NotificationChannel;
import com.notificationsystem.domain.*;
import com.notificationsystem.application.retry.RetryPolicy;
import com.notificationsystem.application.templates.TemplateEngine;

import com.notificationsystem.infrastructure.storage.NotificationHistoryRepository;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Core orchestrator of the Notification System.
 * Responsibilities:
 * 1. Resolve user preferences.
 * 2. Generate content via TemplateEngine.
 * 3. Create appropriate notification instances.
 * 4. Dispatch via appropriate channels using a retry mechanism.
 * 5. Log the outcome.
 */
public class NotificationDispatcher {
    private static final Logger LOGGER = Logger.getLogger(NotificationDispatcher.class.getName());

    private final NotificationPreferenceService preferenceService;
    private final TemplateEngine templateEngine;
    private final Map<ChannelType, NotificationChannel> channelRegistry;
    private final NotificationHistoryRepository historyRepository;

    public NotificationDispatcher(
            NotificationPreferenceService preferenceService,
            TemplateEngine templateEngine,
            Map<ChannelType, NotificationChannel> channelRegistry,
            NotificationHistoryRepository historyRepository) {
        this.preferenceService = preferenceService;
        this.templateEngine = templateEngine;
        this.channelRegistry = channelRegistry;
        this.historyRepository = historyRepository;
    }

    /**
     * Dispatches a notification to the user for a specific event, utilizing the provided retry policy.
     * 
     * @param user the target recipient
     * @param eventType the type of event triggering the notification
     * @param retryPolicy the policy to handle transient sending failures
     */
    public void dispatch(User user, String eventType, RetryPolicy retryPolicy) {
        Set<ChannelType> preferredChannels = preferenceService.getPreferredChannels(user);

        if (preferredChannels.isEmpty()) {
            LOGGER.info("No preferred channels for user: " + user.getId() + ". Notification skipped.");
            return;
        }

        // DECISION: We decouple message generation into a TemplateEngine.
        // Why? This prevents the dispatcher from becoming bloated with string formatting logic
        // and allows templates to be swapped or loaded from a database without changing core routing logic.
        String messageContent = templateEngine.generateMessage(eventType);
        Priority priority = determinePriority(eventType);

        // DECISION: Iterate through all preferred channels instead of just picking one.
        // Why? Users often want critical alerts on multiple platforms (e.g. Email + SMS).
        for (ChannelType channelType : preferredChannels) {
            NotificationChannel channel = channelRegistry.get(channelType);
            if (channel == null) {
                LOGGER.warning("No channel implementation found for: " + channelType);
                continue;
            }

            Notification notification = createNotification(channelType, user.getId(), messageContent, eventType, priority);

            // DECISION: Wrap the sending execution in a Runnable and pass to a RetryPolicy.
            // Why? The Strategy Pattern allows us to swap retry mechanisms (e.g. Exponential vs Linear backoff)
            // at runtime based on the eventType, without polluting the channel classes with retry loops.
            Runnable sendTask = () -> channel.send(notification);

            try {
                retryPolicy.execute(sendTask);
                recordSuccess(user.getId(), eventType, channelType);
            } catch (Exception e) {
                recordFailure(user.getId(), eventType, channelType, e.getMessage());
            }
        }
    }

    /**
     * Factory method to create the concrete notification based on the channel type.
     * DECISION: Centralized factory method.
     * Why? It isolates the creation logic. If we add a SlackNotification, we only update this switch statement.
     */
    private Notification createNotification(ChannelType type, String userId, String message, String template, Priority priority) {
        return switch (type) {
            case EMAIL -> new EmailNotification(userId, message, template, priority);
            case SMS -> new SMSNotification(userId, message, template, priority);
            case PUSH -> new PushNotification(userId, message, template, priority);
        };
    }

    /**
     * Priority logic could be complex. Kept simple here for the domain example.
     */
    private Priority determinePriority(String eventType) {
        if ("SYNC_FAILED".equalsIgnoreCase(eventType) || "SECURITY_ALERT".equalsIgnoreCase(eventType)) {
            return Priority.URGENT;
        }
        return Priority.NORMAL;
    }

    private void recordSuccess(String userId, String eventType, ChannelType channelType) {
        LOGGER.info("Successfully dispatched " + eventType + " to user " + userId + " via " + channelType);
        historyRepository.save(new NotificationHistoryEntry(userId, eventType, channelType, true, null));
    }

    private void recordFailure(String userId, String eventType, ChannelType channelType, String errorMessage) {
        LOGGER.log(Level.SEVERE, "Failed to dispatch " + eventType + " to user " + userId + " via " + channelType + ". Reason: " + errorMessage);
        historyRepository.save(new NotificationHistoryEntry(userId, eventType, channelType, false, errorMessage));
    }
}
