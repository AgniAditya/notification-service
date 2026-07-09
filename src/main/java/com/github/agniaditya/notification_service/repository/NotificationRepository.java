package com.github.agniaditya.notification_service.repository;

import com.github.agniaditya.notification_service.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    Optional<NotificationEntity> findByIdempotencyKey(String idempotency_key);
}
