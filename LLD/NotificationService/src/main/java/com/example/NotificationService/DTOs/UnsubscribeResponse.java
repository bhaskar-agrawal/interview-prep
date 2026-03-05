package com.example.NotificationService.DTOs;

import com.example.NotificationService.Models.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnsubscribeResponse {
    String userId;
    NotificationType type;
    String statusResponse;
}
