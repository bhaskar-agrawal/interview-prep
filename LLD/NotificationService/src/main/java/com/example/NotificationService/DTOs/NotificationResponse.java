package com.example.NotificationService.DTOs;

import com.example.NotificationService.Models.NotificationType;
import com.example.NotificationService.Models.SingleNotification;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NotificationResponse {
    List<SingleNotification> notifications;
}

// userId
// channels
// serviceName
// content.