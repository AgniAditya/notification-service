package com.github.agniaditya.notification_service.entity;

import com.github.agniaditya.notification_service.enums.Channels;
import com.github.agniaditya.notification_service.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private API_Keys client;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private Channels channel;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "failure_reason")
    private String failureReason = null;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}