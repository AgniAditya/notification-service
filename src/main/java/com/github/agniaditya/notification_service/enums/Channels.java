package com.github.agniaditya.notification_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Channels {
    GMAIL, SMS, PUSH;

    @JsonCreator
    public static Channels fromValue(String value) {
        return Channels.valueOf(value.toUpperCase().trim());
    }
}
