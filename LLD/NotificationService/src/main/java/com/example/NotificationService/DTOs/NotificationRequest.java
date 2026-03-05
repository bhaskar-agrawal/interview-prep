package com.example.NotificationService.DTOs;

import com.example.NotificationService.Models.NotificationChannels;
import com.example.NotificationService.Models.NotificationType;
import lombok.Data;

import java.util.List;

@Data
public class NotificationRequest {
    NotificationType type;
    String serviceName;
    String data;
}
