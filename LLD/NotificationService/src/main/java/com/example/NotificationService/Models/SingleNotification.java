package com.example.NotificationService.Models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SingleNotification {
    String userId;
    List<NotificationChannels> notificationChannelsList;
    String data;
    String notificationService;
}
