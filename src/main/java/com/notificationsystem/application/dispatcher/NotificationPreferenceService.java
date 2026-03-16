package com.notificationsystem.application.dispatcher;

import com.notificationsystem.domain.ChannelType;
import com.notificationsystem.domain.User;
import java.util.Set;

/**
 * Service dedicated to resolving which channels a user prefers.
 * Isolating this logic allows future expansion (e.g., checking user timezone,
 * subscription level, or specific event opt-outs) without bloating the user model.
 */
public class NotificationPreferenceService {

    /**
     * Retrieves the preferred channels for a user.
     * @param user the user whose preferences are being evaluated
     * @return the set of preferred notification channels
     */
    public Set<ChannelType> getPreferredChannels(User user) {
        if (user == null || user.getPreferences() == null || user.getPreferences().isEmpty()) {
            return Set.of(); // Return empty set instead of null
        }
        return user.getPreferences();
    }
}
