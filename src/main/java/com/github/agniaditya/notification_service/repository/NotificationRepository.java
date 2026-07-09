package com.github.agniaditya.notification_service.repository;

import com.github.agniaditya.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Optional<Notification> findByIdempotencyKey(String idempotency_key);
}
