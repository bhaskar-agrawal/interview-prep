package com.example.NotificationService.Models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationType {
    SERVICE_UP,
    SERVICE_DOWN,
    HIGH_LOAD,
    LOW_LOAD,
    RIGHT_WORKING;

    @JsonCreator
    public static NotificationType fromString(String value) {
        return NotificationType.valueOf(value.toUpperCase());
    }
}
