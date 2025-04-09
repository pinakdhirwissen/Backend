package com.example.TicketingSystem.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import static org.apache.coyote.http11.Constants.a;

@Service
public class Email2Service {

    @Autowired
    private JavaMailSender mailSender;
  @Async
    public void sendTicketEmail(String recipientEmail, String ticketId, String title, String description,
                                String createdDate, String priority, String dueDate,
                                String status, String subject, String assignee, String createdBy) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Ticket " + ticketId + " - " + subject);

            String emailContent = buildEmailContent(ticketId, title, description, createdDate, priority, dueDate, status, assignee, createdBy);
            helper.setText(emailContent, true); // Enable HTML formatting

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Error sending email to " + recipientEmail + ": " + e.getMessage());
        }
    }

    private String buildEmailContent(String ticketId, String title, String description,
                                     String createdDate, String priority, String dueDate,
                                     String status, String assignee, String createdBy) {
        return "<html>" +
                "<body>" +
                "<h2>Ticket Update</h2>" +
                "<p>" +
                "    <strong>Ticket ID:</strong> " +
                "    <a href='http://localhost:5173/ticket/" + ticketId + "' " +
                "       target='_blank' " +
                "       style='color:blue; text-decoration:underline; cursor:pointer;'>" +
                ticketId +
                "</a>" +
                "</p>"+
                "<p><strong>Title:</strong> " + title + "</p>" +
                "<p><strong>Description:</strong> " + description + "</p>" +
                "<p><strong>Created Date:</strong> " + createdDate + "</p>" +
                "<p><strong>Priority:</strong> " + priority + "</p>" +
                "<p><strong>Due Date:</strong> " + dueDate + "</p>" +
                "<p><strong>Status:</strong> " + status + "</p>" +
                "<p><strong>Assigned To:</strong> " + assignee + "</p>" +
                "<p><strong>Created By:</strong> " + createdBy + "</p>" +
                "<br><hr><p>This is an automated email. Please do not reply.</p>" +
                "</body>" +
                "</html>";
    }
}
