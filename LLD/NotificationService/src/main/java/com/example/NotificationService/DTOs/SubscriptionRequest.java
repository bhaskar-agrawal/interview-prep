package com.example.NotificationService.DTOs;

import com.example.NotificationService.Models.NotificationChannels;
import com.example.NotificationService.Models.NotificationType;
import lombok.Data;

import java.util.List;

@Data
public class SubscriptionRequest {
    String userId;
    NotificationType type;
    List<NotificationChannels> addChannels;
}
