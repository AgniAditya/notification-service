package com.github.agniaditya.notification_service.repository;

import com.github.agniaditya.notification_service.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, String> {
    Optional<Notifications> findByIdempotencyKey(String idempotency_key);
}
