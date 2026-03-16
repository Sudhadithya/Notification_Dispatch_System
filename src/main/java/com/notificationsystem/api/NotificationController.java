package com.notificationsystem.api;

import com.notificationsystem.application.dispatcher.NotificationDispatcher;
import com.notificationsystem.domain.NotificationHistoryEntry;
import com.notificationsystem.domain.User;
import com.notificationsystem.application.retry.ExponentialBackoffPolicy;
import com.notificationsystem.application.retry.RetryPolicy;
import com.notificationsystem.infrastructure.storage.NotificationHistoryRepository;
import com.notificationsystem.infrastructure.storage.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller acting as the thin entry point to the system.
 * Delegates actual business logic to the pure OOP domain layer.
 */
@RestController
@RequestMapping
public class NotificationController {

    private final UserRepository userRepository;
    private final NotificationHistoryRepository historyRepository;
    private final NotificationDispatcher dispatcher;

    public NotificationController(UserRepository userRepository,
            NotificationHistoryRepository historyRepository,
            NotificationDispatcher dispatcher) {
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.dispatcher = dispatcher;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, request.getName(), request.getEmail(), request.getPhoneNumber(),
                request.getPreferences());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/notify")
    public ResponseEntity<String> notifyUser(@RequestBody NotifyRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Using Exponential Backoff for this trigger
        // In a real system, the policy could be selected based on eventType or channel
        RetryPolicy retryPolicy = new ExponentialBackoffPolicy(3, 1000);

        dispatcher.dispatch(user, request.getEventType(), retryPolicy);

        return ResponseEntity.ok("Dispatch triggered for " + request.getEventType());
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<NotificationHistoryEntry>> getNotifications(@PathVariable String userId) {
        List<NotificationHistoryEntry> history = historyRepository.findByUserId(userId);
        return ResponseEntity.ok(history);
    }

    // --- Helper endpoints to check the Database contents ---

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/history")
    public ResponseEntity<List<NotificationHistoryEntry>> getAllHistory() {
        return ResponseEntity.ok(historyRepository.findAll());
    }
}
