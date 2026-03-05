package com.example.NotificationService.Service;

import com.example.NotificationService.DTOs.*;
import com.example.NotificationService.Models.Notification;
import com.example.NotificationService.Models.NotificationChannels;
import com.example.NotificationService.Models.NotificationType;
import com.example.NotificationService.Models.Notifier.NotifierFactory;
import com.example.NotificationService.Models.SingleNotification;
import com.example.NotificationService.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private NotificationRepository repo;
    private NotifierFactory factory;
    public NotificationService(NotificationRepository repo, NotifierFactory factory) {
        this.repo = repo;
        this.factory = factory;
    }

    public SubscriptionResponse subscribe(SubscriptionRequest request) {
        String userId = request.getUserId();
        NotificationType type = request.getType();
        List<NotificationChannels> channels = request.getAddChannels();
        repo.subscribe(userId, type, channels);
        return SubscriptionResponse.builder()
                .userId(userId)
                .type(type)
                .statusResponse("subscribe success")
                .build();
    }

    public UnsubscribeResponse unsubscribe(UnSubscribeRequest request) {
        String userId = request.getUserId();
        NotificationType type = request.getType();
        repo.unsubscribe(userId, type);
        return UnsubscribeResponse.builder()
                .userId(userId)
                .type(type)
                .statusResponse("unsubscribe success")
                .build();
    }

    public List<String> getUsers(NotificationType type) {
        return repo.getUsers(type);
    }

    public NotificationResponse notify(NotificationRequest request) throws IOException {
        NotificationType type = request.getType();
        String data = request.getData();
        String serviceName = request.getServiceName();
        List<String> users = repo.getUsers(type);
        List<SingleNotification> notificationsList = new ArrayList<>();
        for(String userId: users) {
            SingleNotification notfn = SingleNotification.builder()
                    .userId(userId)
                    .notificationChannelsList(repo.channelsForUser(userId))
                    .data(data)
                    .notificationService(serviceName)
                    .build();
            Notification notification = Notification.builder()
                            .data(data)
                            .serviceName(serviceName)
                            .build();
            List<NotificationChannels> channels = repo.channelsForUser(userId);
            for(NotificationChannels channel: channels) {
                factory.getObject(channel).notify(userId, notification);
            }
            notificationsList.add(notfn);
        }
        return NotificationResponse.builder()
                .notifications(notificationsList)
                .build();
    }
}
