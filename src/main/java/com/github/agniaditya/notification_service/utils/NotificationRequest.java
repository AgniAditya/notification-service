package com.github.agniaditya.notification_service.utils;

import com.github.agniaditya.notification_service.enums.Channels;

public class NotificationRequest {
    private String content;
    private Channels channel;
    private String recipient;

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Channels getChannel() {
        return channel;
    }

    public void setChannel(Channels channel) {
        this.channel = channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
