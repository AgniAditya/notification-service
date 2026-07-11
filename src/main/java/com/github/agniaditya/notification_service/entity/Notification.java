package com.github.agniaditya.notification_service.entity;

import com.github.agniaditya.notification_service.enums.Channels;
import com.github.agniaditya.notification_service.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ApiKey clientId;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private Channels channel;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason = null;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt = null;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void setClientId(ApiKey clientId) {
        this.clientId = clientId;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChannel(Channels channel) {
        this.channel = channel;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Status getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public ApiKey getClientId() {
        return clientId;
    }

    public String getRecipient() {
        return recipient;
    }

    public Channels getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}