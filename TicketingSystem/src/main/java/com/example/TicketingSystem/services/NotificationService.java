
package com.example.TicketingSystem.services;

import com.example.TicketingSystem.models.Notification;
import com.example.TicketingSystem.repositories.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteReadNotificationsEveryMinute() {
        notificationRepository.deleteByIsReadTrue();
        System.out.println("✅ Deleted all read notifications (every minute).");
    }

    public void sendNotification(String emailId, String message) {
        // ✅ Save notification in the database
        Notification notification = new Notification(emailId, message, false, LocalDateTime.now());
        notificationRepository.save(notification);

        // ✅ Send real-time WebSocket message (correct destination)
        messagingTemplate.convertAndSendToUser(emailId, "/queue/notifications", notification);
    }
    public boolean markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }


}
