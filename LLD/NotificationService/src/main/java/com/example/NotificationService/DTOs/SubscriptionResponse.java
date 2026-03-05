package com.example.NotificationService.DTOs;

import com.example.NotificationService.Models.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResponse {
    String userId;
    String statusResponse;
    NotificationType type;
}
