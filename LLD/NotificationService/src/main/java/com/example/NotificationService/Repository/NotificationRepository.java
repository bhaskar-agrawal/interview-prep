package com.example.NotificationService.Repository;

import com.example.NotificationService.Models.Notification;
import com.example.NotificationService.Models.NotificationChannels;
import com.example.NotificationService.Models.NotificationType;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class NotificationRepository {

    private Map<NotificationType, Set<String>> notfnTypeUsersMapping;
    private Map<String, Set<NotificationChannels>> userIdChannelsMapping;

    public NotificationRepository() {
        this.notfnTypeUsersMapping = new ConcurrentHashMap<>();
        this.userIdChannelsMapping = new ConcurrentHashMap<>();
    }

    public void subscribe(String userId, NotificationType type, List<NotificationChannels> channels) {
        notfnTypeUsersMapping.computeIfAbsent(type, key -> {
            Set<String> st = new HashSet<>();
            st.add(userId);
            return st;
        });
        notfnTypeUsersMapping.computeIfPresent(type, (key,value) -> {
            value.add(userId);
            return value;
        });
        // interleaving can happen lock can be used, skipping for now. The new channels can be added, older one will be there.
        userIdChannelsMapping.computeIfAbsent(userId, key -> {
            Set<NotificationChannels> st = new HashSet<>(channels);
            return st;
        });
        userIdChannelsMapping.computeIfPresent(userId, (key,value) -> {
            value.addAll(channels);
            return value;
        });
    }
    // limtitation that userId, last update will be used to send the data to specific channels.

    public void unsubscribe(String userId, NotificationType type) {
        notfnTypeUsersMapping.computeIfPresent(type, (key, value) -> {
            value.remove(userId);
            return value;
        });
        // keeping the same subscription channel for user.
    }

    public List<String> getUsers(NotificationType type) {
        if(!this.notfnTypeUsersMapping.containsKey(type)) {
            return new ArrayList<>();
        }
        return this.notfnTypeUsersMapping.get(type).stream().toList();
    }

    public List<NotificationChannels> channelsForUser(String userId) {
        return this.userIdChannelsMapping.get(userId).stream().toList();
    }
}
