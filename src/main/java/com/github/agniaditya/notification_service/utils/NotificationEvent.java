package com.github.agniaditya.notification_service.utils;

import com.github.agniaditya.notification_service.enums.Channels;

public record NotificationEvent(
        String notificationId,
        String content,
        String recipient,
        Channels channel,
        int retryCount
) {}
