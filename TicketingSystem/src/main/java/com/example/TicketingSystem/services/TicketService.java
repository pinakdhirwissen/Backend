package com.example.TicketingSystem.services;

import com.example.TicketingSystem.models.Tickets;
import com.example.TicketingSystem.models.TicketDepartment;
import com.example.TicketingSystem.repositories.TicketRepository;
import com.example.TicketingSystem.repositories.TicketDepartmentRepository;
import com.example.TicketingSystem.services.NotificationService;
import com.example.TicketingSystem.services.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketDepartmentRepository ticketDepartmentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    public Optional<Tickets> getTicketById(String ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public Tickets createTicket(Tickets ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Tickets> getTicketsByCreatedBy(String createdBy) {
        return ticketRepository.findByCreatedBy(createdBy);
    }

    public List<Tickets> getTicketsByAssignTo(String assignTo) {
        return ticketRepository.findByAssignTo(assignTo);
    }

    public Optional<Tickets> updateTicket(String ticketId, Tickets updatedTicket) {
        return ticketRepository.findById(ticketId).map(existingTicket -> {
            existingTicket.setTitle(updatedTicket.getTitle());
            existingTicket.setDescription(updatedTicket.getDescription());
            existingTicket.setDepartment(updatedTicket.getDepartment());
            existingTicket.setAssignTo(updatedTicket.getAssignTo());
            existingTicket.setPriority(updatedTicket.getPriority());
            existingTicket.setDueDate(updatedTicket.getDueDate());
            existingTicket.setUpdatedBy(updatedTicket.getUpdatedBy());
            return ticketRepository.save(existingTicket);
        });
    }

    public String getEmailFromTicketId(String ticketId) {
        return ticketRepository.findEmailByTicketId(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("No email found for this ticket"));
    }

    public Tickets findByTicketId(String ticketId) {
        return ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with ID: " + ticketId));
    }

    public List<Tickets> searchTickets(String createdBy, String assignTo) {
        return ticketRepository.findByCreatedByOrAssignTo(createdBy, assignTo);
    }
    @Transactional
    @Scheduled(fixedRate = 900000) // Runs every 15 minutes
    public void assignUnassignedTickets() {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        List<Tickets> openTickets = ticketRepository.findByStatusAndCreatedDateBeforeAndAssignToIsNull("Open", fifteenMinutesAgo);

        for (Tickets ticket : openTickets) {
            String department = ticket.getDepartment();
            List<TicketDepartment> activeUsers = ticketDepartmentRepository.findByDepartment(department)
                    .stream()
                    .filter(TicketDepartment::getIsActive)
                    .toList();

            if (activeUsers.isEmpty()) {
                System.out.println("No active users in department: " + department);
                continue;
            }

            Optional<TicketDepartment> leastBusyUser = activeUsers.stream()
                    .min(Comparator.comparingInt(user -> ticketRepository.countByAssignTo(user.getEmailId())));

            if (leastBusyUser.isPresent()) {
                String assigneeEmail = leastBusyUser.get().getEmailId();

                Optional<Tickets> latestTicket = ticketRepository.findById(ticket.getTicketId());
                if (latestTicket.isPresent() && latestTicket.get().getAssignTo() == null) {
                    ticket.setAssignTo(assigneeEmail);
                    ticket.setStatus("Assigned");
                    ticket.setUpdatedDate(LocalDateTime.now());
                    ticket.setUpdatedBy(assigneeEmail);
                    ticketRepository.save(ticket);

                    notificationService.sendNotification(assigneeEmail,
                            "You have been assigned a new ticket (ID: " + ticket.getTicketId() + ").");

                    emailService.sendTicketEmail(
                            assigneeEmail, ticket.getTicketId(), ticket.getTitle(), ticket.getDescription(),
                            ticket.getCreatedDate().toString(), ticket.getPriority(), ticket.getDueDate().toString(),
                            "Assigned", false, "You have been assigned a new ticket"
                    );


                    System.out.println("Ticket ID: " + ticket.getTicketId() + " assigned to " + assigneeEmail);
                }
            } else {
                System.out.println("No available user for ticket ID: " + ticket.getTicketId());
            }
        }
    }
}