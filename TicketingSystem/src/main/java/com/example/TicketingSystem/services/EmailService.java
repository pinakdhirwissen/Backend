package com.example.TicketingSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;
    @Async
    public void sendTicketEmail(String recipient, String ticketId, String title, String description,
                                String createdDate, String priority, String dueDate, String status,boolean isCreator,String emailheader) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set email properties
            String subject = isCreator ? "You Created a New Request" : "New Request in Department";
            helper.setTo(recipient);
            helper.setSubject(subject);

            // Prepare template context
            Context context = new Context();
            System.out.print("email header is: "+emailheader);
            context.setVariable("header", emailheader);
            context.setVariable("ticketId", ticketId);
            context.setVariable("title", title);
            context.setVariable("description", description);
            context.setVariable("createdDate", createdDate);
            context.setVariable("priority", priority);
            context.setVariable("dueDate", dueDate);
            context.setVariable("status", status);

            // Process Thymeleaf template
            String htmlContent = templateEngine.process("ticket-email", context);
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}













