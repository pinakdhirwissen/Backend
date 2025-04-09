package com.example.TicketingSystem.controllers;
import com.example.TicketingSystem.models.TicketDepartment;
import com.example.TicketingSystem.services.*;
import com.example.TicketingSystem.models.Tickets;
import com.example.TicketingSystem.repositories.TicketRepository;
import com.example.TicketingSystem.repositories.TicketDepartmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractAuditable_.createdBy;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Management", description = "APIs for managing tickets")  // Grouping APIs under "Ticket Management"
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketDepartmentRepository ticketDepartmentRepository;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private Email2Service email2Service;
    @Autowired
    private Email4Service email4Service;



    @Operation(summary = "Get Ticket by ID", description = "Fetch a ticket by its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping("/{ticketId}")
    public ResponseEntity<Tickets> getTicketById(
            @Parameter(description = "ID of the ticket to be retrieved") @PathVariable String ticketId) {
        return ticketService.getTicketById(ticketId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Tickets> createTicket(@Valid @RequestBody Tickets ticket) {

        Tickets savedTicket = ticketService.createTicket(ticket);
        DateTimeFormatter createdDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        DateTimeFormatter dueDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ticketLink = "http://localhost:5173/ticket/" +savedTicket.getTicketId();

        // Format the createdDate and dueDate
        String formattedCreatedDate = savedTicket.getCreatedDate().format(createdDateFormatter);
        String formattedDueDate = savedTicket.getDueDate().format(dueDateFormatter);
        emailService.sendTicketEmail(
                savedTicket.getCreatedBy(),     // Recipient's email
                savedTicket.getTicketId(),     // Ticket ID
                savedTicket.getTitle(),
                savedTicket.getDescription(),
                formattedCreatedDate,
                savedTicket.getPriority(),
                formattedDueDate,
                savedTicket.getStatus(),
                true,   "New Ticket Created"
        );
        notificationService.sendNotification(savedTicket.getCreatedBy(),
                "You Created  " + savedTicket.getTicketId()+ " new request"
        );

        // ✅ Find active users in the creator's department
        String department = ticket.getDepartment();
        List<TicketDepartment> activeDepartmentUsers = ticketDepartmentRepository.findByDepartment(department)
                .stream()
                .filter(TicketDepartment::getIsActive) // Ensure only active users are selected

                // Exclude the creator
                .toList();

        // ✅ Send email to all active department members
        for (TicketDepartment member : activeDepartmentUsers) {
            if (!member.getEmailId().equals(savedTicket.getCreatedBy())) {
                System.out.print(member+"mem"+savedTicket.getCreatedBy());
                emailService.sendTicketEmail(
                        member.getEmailId(), // Send email to department members
                        savedTicket.getTicketId(),
                        savedTicket.getTitle(),
                        savedTicket.getDescription(),
                        formattedCreatedDate,
                        savedTicket.getPriority(),
                        formattedDueDate,
                        savedTicket.getStatus(),
                        false,
                        "New Ticket in Department"
                );
            }
            notificationService.sendNotification(member.getEmailId(),
                    "A new ticket (ID: " + savedTicket.getTicketId()+ ") has been created in your department."
            );
        } // Send notification to department members

        return ResponseEntity.ok(savedTicket);
    }




    @Operation(summary = "Get Tickets by Creator", description = "Retrieve all tickets created by a specific user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully")
    })
    @GetMapping("/createdBy/{email}")
    public ResponseEntity<List<Tickets>> getTicketsByCreatedBy(
            @Parameter(description = "Email of the user who created the tickets") @PathVariable String email) {
        List<Tickets> tickets = ticketService.getTicketsByCreatedBy(email);
        return ResponseEntity.ok(tickets);
    }



    @Operation(summary = "Get Tickets Assigned to User", description = "Retrieve all tickets assigned to a specific user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully")
    })
    @GetMapping("/assignTo/{email}")
    public ResponseEntity<List<Tickets>> getTicketsByAssignTo(
            @Parameter(description = "Email of the user assigned to the tickets") @PathVariable String email) {
        List<Tickets> tickets = ticketService.getTicketsByAssignTo(email);
        return ResponseEntity.ok(tickets);
    }


    @GetMapping("/search/{email}")
    public ResponseEntity<List<Tickets>> searchTickets(
         @PathVariable String email) {
        List<Tickets> tickets = ticketService.searchTickets(email, email);
        if(tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }


    @Operation(summary = "Update a Ticket", description = "Update ticket details by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket updated successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping("/{ticketId}")
    public ResponseEntity<?> updateTicket(
            @Parameter(description = "ID of the ticket to be updated") @PathVariable String ticketId,
            @Valid @RequestBody Tickets updatedTicket) {
        return ticketService.updateTicket(ticketId, updatedTicket)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get Tickets by Department", description = "Retrieve all tickets related to a specific department.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully")
    })
    @GetMapping("/department/{department}")
    public List<Tickets> getTicketsByDepartment(
            @Parameter(description = "Department to filter tickets") @PathVariable String department) {
        return ticketRepository.findByDepartment(department);
    }


    @PutMapping("/{ticketId}/assign")
    public ResponseEntity<Tickets> assignTicket(
            @PathVariable String ticketId,
            @RequestParam String assigneeEmail
    ) { String ticketLink = "http://localhost:5173/ticket/" + ticketId;
        Optional<Tickets> optionalTicket = ticketRepository.findByTicketId(ticketId);
        if (optionalTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tickets ticket = optionalTicket.get();
        if (ticket.getAssignTo() != null) {
            return ResponseEntity.badRequest().body(ticket); // Already assigned
        }

        ticket.setAssignTo(assigneeEmail);
        ticket.setStatus("Assigned");
        ticket.setUpdatedDate(LocalDateTime.now());
        ticket.setUpdatedBy(assigneeEmail);
        ticketRepository.save(ticket);

        // ✅ Format dates before passing to email service
        String formattedCreatedDate = ticket.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String formattedDueDate = ticket.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy "));

        // 🔥 Send emails dynamically
        email2Service.sendTicketEmail(
                assigneeEmail ,ticket.getTicketId(), ticket.getTitle(), ticket.getDescription(),
                formattedCreatedDate, ticket.getPriority(), formattedDueDate,
                "Assigned", "Assigned", assigneeEmail, ticket.getCreatedBy()
        );

        email2Service.sendTicketEmail(
                ticket.getCreatedBy(), ticket.getTicketId(), ticket.getTitle(), ticket.getDescription(),
                formattedCreatedDate, ticket.getPriority(), formattedDueDate,
                "Assigned", "Status Changed to Assigned", assigneeEmail, ticket.getCreatedBy()
        );
        notificationService.sendNotification(assigneeEmail,
                "You have been assigned a new ticket (ID: " + ticket.getTicketId()+ ").");

        notificationService.sendNotification(ticket.getCreatedBy(),
                "Your ticket (ID: " +ticket.getTicketId()+ ") has been assigned to " + assigneeEmail + ".");


        return ResponseEntity.ok(ticket);
    }


    @PutMapping("/{ticketId}/close")
    public ResponseEntity<Tickets> closeTicket(@PathVariable String ticketId) {
        Optional<Tickets> optionalTicket = ticketRepository.findByTicketId(ticketId);
        if (optionalTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tickets ticket = optionalTicket.get();
        if (!"Assigned".equals(ticket.getStatus())) {
            return ResponseEntity.badRequest().body(ticket); // Cannot close if not assigned
        }

        ticket.setStatus("Closed");
        ticket.setUpdatedDate(LocalDateTime.now());
        ticketRepository.save(ticket);

        // ✅ Format dates before passing to email service
        String formattedCreatedDate = ticket.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String formattedDueDate = ticket.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy "));
        String feedbackFormUrl = "http://localhost:5173/feedback?ticketId=" + ticketId;String ticketLink = "http://localhost:5173/ticket/" + ticketId;

        // 🔥 Send closure emails dynamically
        email2Service.sendTicketEmail(
                ticket.getCreatedBy(), ticket.getTicketId(), ticket.getTitle(), ticket.getDescription(),
                formattedCreatedDate, ticket.getPriority(), formattedDueDate,
                "Closed", "Your Ticket is Closed", ticket.getAssignTo(), ticket.getCreatedBy()
        );
        System.out.println("Sending ticket email to Assignee: " + ticket.getAssignTo());
        System.out.println("Sending ticket email to Assignee: " + ticket.getCreatedBy());
        email2Service.sendTicketEmail(
                ticket.getAssignTo(), ticket.getTicketId(), ticket.getTitle(), ticket.getDescription(),
                formattedCreatedDate, ticket.getPriority(), formattedDueDate,
                "Closed", "Status Changed to Closed", ticket.getAssignTo(), ticket.getCreatedBy()
        );
        System.out.print(feedbackFormUrl);
        email4Service.sendFeedbackRequestEmail(
                ticket.getCreatedBy(), ticket.getTicketId(), ticket.getTitle(),
                "Your ticket has been closed. Please provide feedback for the assignee: " + ticket.getAssignTo() +
                        "\nClick here to submit feedback: "+feedbackFormUrl+"your feedback"
        );
        // ✅ Send real-time notifications
        notificationService.sendNotification(ticket.getCreatedBy(),
                "Your ticket (ID: " + ticket.getTicketId()+ ") has been closed.");

        notificationService.sendNotification(ticket.getAssignTo(),
                "The ticket (ID: " + ticket.getTicketId()+ ") assigned to you has been closed.");




        return ResponseEntity.ok(ticket);
    }
    @PutMapping("/{ticketId}/reopen")
    public ResponseEntity<Tickets> reopenTicket(@PathVariable String ticketId) {
        String ticketLink = "http://localhost:5173/ticket/" + ticketId;
        Optional<Tickets> optionalTicket = ticketRepository.findByTicketId(ticketId);
        if (optionalTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tickets ticket = optionalTicket.get();
        if (!"Closed".equals(ticket.getStatus())) {
            return ResponseEntity.badRequest().body(ticket); // Can only reopen if closed
        }

        ticket.setStatus("Open");
        ticket.setAssignTo(null); // Reset assignee
        ticket.setUpdatedDate(LocalDateTime.now()); // ✅ Update the updatedTime
        ticketRepository.save(ticket);

        // ✅ Notify the creator that the ticket is reopened
        email2Service.sendTicketEmail(
                ticket.getCreatedBy(), ticket.getTicketId(), ticket.getTitle(), ticket.getDescription(),
                ticket.getCreatedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                ticket.getPriority(),
                ticket.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy ")),
                "Reopened", "Your ticket has been reopened", null, ticket.getCreatedBy()
        );
        // ✅ Send real-time notifications to creator & previous assignee
        notificationService.sendNotification(ticket.getCreatedBy(),
                "Your ticket (ID: " + ticket.getTicketId()+ ") has been reopened.");

        return ResponseEntity.ok(ticket);
    }


}