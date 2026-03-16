package com.notificationsystem.config;

import com.notificationsystem.infrastructure.channels.EmailChannel;
import com.notificationsystem.infrastructure.channels.NotificationChannel;
import com.notificationsystem.infrastructure.channels.PushChannel;
import com.notificationsystem.infrastructure.channels.SMSChannel;
import com.notificationsystem.application.dispatcher.NotificationDispatcher;
import com.notificationsystem.application.dispatcher.NotificationPreferenceService;
import com.notificationsystem.domain.ChannelType;
import com.notificationsystem.infrastructure.storage.NotificationHistoryRepository;
import com.notificationsystem.application.templates.TemplateEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

/**
 * Spring configuration class.
 * DECISION: Manually wire the Domain and Application objects here.
 * Why? By manually creating `@Bean` instances here, our `domain` and `application` layers 
 * remain 100% pure Java. They don't need any Spring `@Service` or `@Autowired` annotations, 
 * which keeps them fully decoupled from the framework (a core tenet of Clean Architecture).
 */
@Configuration
public class NotificationConfig {

    @Bean
    public NotificationPreferenceService notificationPreferenceService() {
        return new NotificationPreferenceService();
    }

    @Bean
    public TemplateEngine templateEngine() {
        return new TemplateEngine();
    }

    @Bean
    public NotificationDispatcher notificationDispatcher(
            NotificationPreferenceService preferenceService,
            TemplateEngine templateEngine,
            NotificationHistoryRepository historyRepository) {

        // Setup the channel registry using an EnumMap for efficiency
        Map<ChannelType, NotificationChannel> channels = new EnumMap<>(ChannelType.class);
        channels.put(ChannelType.EMAIL, new EmailChannel());
        channels.put(ChannelType.SMS, new SMSChannel());
        channels.put(ChannelType.PUSH, new PushChannel());

        // We pass historyRepository directly so the pure OOP dispatcher can append to the history list.
        // In a real database app, we would inject a domain-specific Data Access interface.
        return new NotificationDispatcher(preferenceService, templateEngine, channels, historyRepository);
    }
}
