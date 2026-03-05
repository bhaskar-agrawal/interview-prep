package com.example.NotificationService.Models.Notifier;

import com.example.NotificationService.Models.Notification;

public interface INotifier {
    void notify(String userId,  Notification notification);
}
