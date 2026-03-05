package com.example.NotificationService.Models.Notifier;

import com.example.NotificationService.Models.Notification;
import com.example.NotificationService.Models.NotificationChannels;
import com.example.NotificationService.Models.NotificationType;
import com.example.NotificationService.Utils.FileProcessor;
import org.springframework.stereotype.Component;

@Component
public class NotifierFactory {
    EmailNotifier emailNotifier;
    SlackNotifier slackNotifier;
    WhatsappNotifier whatsappNotifier;
    FileProcessor processor;

    public NotifierFactory(FileProcessor processor) {
        this.emailNotifier = new EmailNotifier(processor);
        this.slackNotifier = new SlackNotifier(processor);
        this.whatsappNotifier = new WhatsappNotifier(processor);
    }

    public AbstractNotifier getObject(NotificationChannels type) {
        if(type==NotificationChannels.MAIL) {
            return this.emailNotifier;
        }
        else if(type==NotificationChannels.SLACK) {
            return this.slackNotifier;
        }
        else {
            return this.whatsappNotifier;
        }
    }
}
