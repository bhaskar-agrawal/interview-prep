Notification service:

Requirements:
1. users can subscribe to diff/ notifications
2. notification has content, service and notification types
3. different channels supported: email, slack and whatsapp
4. create proper apis for subscribe, unsubscribe for a specific user.
5. the notification types are fixed for starting scenario. 

extra requirements:
1. code testable over postman

proper methods:
1. sendNotification(): proper notification is sent to specific subscribed users
  -> a file is created for each notification id: which will have info as:
  notfnId: userId: channel: content.
  each user is separated by new line. 
2. subscribe(userId, notfntype): subscribing to specific kind of events
3. unsubscribe(userId, notfnType): the user can unsubscribe.

entities:
1. userId
2. Notification
3. NotfnChannel
4. NotificationType enum
5. NotificationStrategy: abstract class. Implemented by SlackNotificationStrategy, etc.

different classes:
1. NotificationController
2. NotificationService
3. NotificationRepository
4. Notification
5. NotificationChannel: enum
6. NotificationType: enum
7. NotificationStrategy: factory for different kind of strategy, to be created.

APIs:
1. subscribe(): userId, notfnType
  - 201 created, userId, notfnType response, eventType

2. unsubscribe(): userId, notfnType
  - 201 created, userId, notfnType response, eventTYpe: unsubscribe

3. notify(): notification sent
  - 201 created, create a file with all the notifications and print messages. 

class design:

Notification {
  string content;
  notfnType
  string service;
}

NotfnChannel {
 SLACK, MAIL, WHATSAPP
}

NotificationRepository {
  ConcurrentMap<String, Set<UserId>> notificationsMapping;
  ConcurrentMap<UserId, Set<Channels> chanels> userIdChannelMap;
  + subscribe(UserId, notfnType)
  + unsubscribe(userId, notfnType)
  + getUsers(notfnType) : List<userId>
  + getChannels(userId): List<Channel>
}

NotfnService {
  - notfnRepo: NotificationRepository
  + subscribe(UserId, notfnType)
  + unsubscribe(userId, notfnType)
  + notifyUsers(notfn) : get the users, then notify them on various channel, notify them
}

COntroller interacts with notfn-service.

----
Concurrency discussion:
1. use of concurrrent hashmap in notfnRepository, it is already singleton