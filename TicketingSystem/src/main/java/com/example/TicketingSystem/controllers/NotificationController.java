package com.example.TicketingSystem.controllers;

import com.example.TicketingSystem.models.Notification;
import com.example.TicketingSystem.repositories.NotificationRepository;
import com.example.TicketingSystem.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService; // ✅ Inject NotificationService

    public NotificationController(NotificationRepository notificationRepository, NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    @PutMapping("read/{id}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id){
        boolean updated=notificationService.markAsRead(id);
        if(updated) {
            return ResponseEntity.ok("Notification marked as read");
        }
        else{
            return ResponseEntity.status(404).body("Notification not found");
        }
    }
    // ✅ Get unread notifications for a user
    @GetMapping("/{emailId}")
    public List<Notification> getUnreadNotifications(@PathVariable String emailId) {
        return notificationRepository.findByEmailIdAndIsReadFalse(emailId);
    }

    // ✅ Mark notifications as read
    @PostMapping("/{emailId}/mark-read")
    public void markNotificationsAsRead(@PathVariable String emailId) {
        List<Notification> notifications = notificationRepository.findByEmailIdAndIsReadFalse(emailId);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }

    // ✅ New endpoint to create a notification
    @PostMapping("/{emailId}/send")
    public void sendNotification(@PathVariable String emailId, @RequestBody String message) {
        notificationService.sendNotification(emailId, message); // ✅ Call NotificationService
    }
}
