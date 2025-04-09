package com.example.TicketingSystem.repositories;

import com.example.TicketingSystem.models.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEmailIdAndIsReadFalse(String emailId);
    void deleteByIsReadTrue();
}
