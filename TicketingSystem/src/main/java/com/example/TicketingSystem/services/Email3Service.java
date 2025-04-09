package com.example.TicketingSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class Email3Service {

    @Autowired
    private JavaMailSender mailSender;
   @Async
    public void sendCommentEmail(String recipientEmail, String ticketId, Long commentId, String comment, String commentedBy, boolean isEdited) {
        String subject = isEdited ? "Comment Edited on Ticket #" + ticketId : "New Comment on Ticket #" + ticketId;
        String message = isEdited ?
                "Hello,\n\nA comment has been edited on Ticket #" + ticketId + ".\n\n" +
                        "Comment ID: " + commentId + "\n" +
                        "Edited Comment: " + comment + "\n" +
                        "Edited By: " + commentedBy + "\n\n" +
                        "Please check the ticket for more details."
                :
                "Hello,\n\nA new comment has been added on Ticket #" + ticketId + ".\n\n" +
                        "Comment ID: " + commentId + "\n" +
                        "Comment: " + comment + "\n" +
                        "Commented By: " + commentedBy + "\n\n" +
                        "Please check the ticket for more details.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
