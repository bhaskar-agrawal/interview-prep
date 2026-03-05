package com.example.NotificationService.Models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {
    String data;
    String serviceName;
}
