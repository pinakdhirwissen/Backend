package com.example.TicketingSystem.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;


@Service
public class Email4Service {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ Sends a feedback request email to the ticket creator
    @Async
    public void sendFeedbackRequestEmail(String recipient, String ticketId, String title, String message) {
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

            helper.setTo(recipient);
            helper.setSubject("Feedback Request for Ticket #" + ticketId);
            helper.setText("<p>Dear User,</p>"
                            + "<p>Your ticket <b>" + title + "</b> has been closed.</p>"
                            + "<p>Please provide feedback: </p>"
                            + "<a href='http://localhost:5713?ticketId=" + ticketId + "'>Submit Feedback</a>",
                    true);

            mailSender.send(mailMessage);
            System.out.println("✅ Feedback request sent to " + recipient);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error sending feedback request.");
        }
    }

    // ✅ Sends feedback email to the assignee after submission
    @Async
    public void sendFeedbackToAssignee(String assigneeEmail, String ticketId, String rating, String comments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(assigneeEmail);
            helper.setSubject("Feedback Received for Ticket" );

            String emailContent = "<h3>Feedback for Ticket #" + ticketId + "</h3>"
                    + "<p><b>Rating:</b> " + rating + "/5</p>"
                    + "<p><b>Comments:</b> " + comments + "</p>"
                    + "<br><p>Thank you for your service!</p>";

            helper.setText(emailContent, true);

            mailSender.send(message);
            System.out.println("✅ Feedback email sent to " + assigneeEmail);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error sending feedback email.");
        }
    }
}
