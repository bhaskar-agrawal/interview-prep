package com.example.NotificationService.Models.Notifier;

import com.example.NotificationService.Models.Notification;
import com.example.NotificationService.Utils.FileProcessor;

import java.io.File;
import java.io.IOException;

public class EmailNotifier extends AbstractNotifier {

    public EmailNotifier(FileProcessor processor) {
        super(processor);
    }

    public void notify(String userId,  Notification notification) throws IOException {
        String medium = "Email";
        String message = "userId: "+userId+"; medium:" + medium+ notification.toString();
        this.processor.processMessage(message);
    }
}
