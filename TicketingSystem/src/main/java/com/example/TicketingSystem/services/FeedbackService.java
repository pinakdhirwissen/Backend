package com.example.TicketingSystem.services;

import com.example.TicketingSystem.models.Feedback;
import com.example.TicketingSystem.models.Tickets;
import com.example.TicketingSystem.repositories.FeedbackRepository;
import com.example.TicketingSystem.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private Email4Service email4Service;

    @Autowired
    private TicketRepository ticketRepository;

    public Feedback submitFeedback(Feedback feedback) {
        // Fetch the Ticket from the da tabase by ticketId
        Tickets ticket = ticketRepository.findByTicketId(feedback.getTicketId().getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Now that the Ticket is fetched from the DB, set it to the Feedback entity
        feedback.setTicketId(ticket);
        email4Service.sendFeedbackToAssignee(
                feedback.getAssigneeEmail(),
                String.valueOf(feedback.getTicketId()),
                  feedback.getRating(),
                feedback.getComments()
        );
        // Save the Feedback to the database (Feedback is now linked to a persisted Ticket)
        return feedbackRepository.save(feedback);
    }
}
