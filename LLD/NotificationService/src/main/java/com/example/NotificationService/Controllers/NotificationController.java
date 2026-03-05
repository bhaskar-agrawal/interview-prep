package com.example.NotificationService.Controllers;

import com.example.NotificationService.DTOs.*;
import com.example.NotificationService.Models.NotificationType;
import com.example.NotificationService.Service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class NotificationController {
    private NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    @RequestMapping("/subscribe")
    public ResponseEntity<SubscriptionResponse> subscribe(@RequestBody  SubscriptionRequest request) {
        try {
            SubscriptionResponse response = this.service.subscribe(request);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @RequestMapping("/unsubscribe")
    public ResponseEntity<UnsubscribeResponse> unsubscribe(@RequestBody UnSubscribeRequest request) {
        try {
            UnsubscribeResponse response = this.service.unsubscribe(request);
            return ResponseEntity.ok().body(response);
        }
        catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @RequestMapping("/users")
    public ResponseEntity<List<String>> getUsers(@RequestParam(required = true) NotificationType type) {
        try {
            List<String> users = service.getUsers(type);
            return ResponseEntity.ok(users);
        }
        catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @RequestMapping("/notify")
    public ResponseEntity<NotificationResponse> notify(@RequestBody NotificationRequest request) {
        try {
            NotificationResponse response = service.notify(request);
            return ResponseEntity.ok(response);
        }
        catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
