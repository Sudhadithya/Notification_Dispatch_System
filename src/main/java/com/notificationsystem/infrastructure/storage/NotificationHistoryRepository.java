package com.notificationsystem.infrastructure.storage;

import com.notificationsystem.domain.NotificationHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistoryEntry, Long> {
    List<NotificationHistoryEntry> findByUserId(String userId);
}
