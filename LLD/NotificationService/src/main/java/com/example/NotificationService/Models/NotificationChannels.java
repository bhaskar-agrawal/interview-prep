package com.example.NotificationService.Models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationChannels {
    SLACK, WHATSAPP, MAIL;

    @JsonCreator
    public static NotificationChannels fromString(String value) {
        return NotificationChannels.valueOf(value.toUpperCase());
    }
}
