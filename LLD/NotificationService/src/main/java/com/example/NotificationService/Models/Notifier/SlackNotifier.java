package com.example.NotificationService.Models.Notifier;

import com.example.NotificationService.Models.Notification;
import com.example.NotificationService.Utils.FileProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackNotifier extends AbstractNotifier {

    public SlackNotifier(FileProcessor processor) {
        super(processor);
    }

    public void notify(String userId,  Notification notification) throws IOException {
        String medium = "slack";
        String message = "userId: "+userId+"; medium:" + medium+ notification.toString();
        this.processor.processMessage(message);
    }
}
