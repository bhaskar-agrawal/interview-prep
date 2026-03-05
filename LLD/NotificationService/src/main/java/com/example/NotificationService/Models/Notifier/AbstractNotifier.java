package com.example.NotificationService.Models.Notifier;

import com.example.NotificationService.Models.Notification;
import com.example.NotificationService.Utils.FileProcessor;

import java.io.IOException;

public abstract class AbstractNotifier {
    FileProcessor processor;
    public AbstractNotifier(FileProcessor processor) {
        this.processor = processor;
    }

    public  add abstract void notify(String userId, Notification notification) throws IOException;
}
