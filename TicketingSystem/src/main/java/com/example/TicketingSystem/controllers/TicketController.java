package com.example.TicketingSystem.controllers;

import com.example.TicketingSystem.models.Tickets;
import com.example.TicketingSystem.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Tickets> createTicket(@Valid @RequestBody Tickets ticket) {
        Tickets savedTicket = ticketService.createTicket(ticket);
        return ResponseEntity.ok(savedTicket);
    }

    @GetMapping("/createdBy/{email}")
    public ResponseEntity<List<Tickets>> getTicketsByCreatedBy(@PathVariable String email) {
        List<Tickets> tickets = ticketService.getTicketsByCreatedBy(email);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/assignTo/{email}")
    public ResponseEntity<List<Tickets>> getTicketsByAssignTo(@PathVariable String email) {
        List<Tickets> tickets = ticketService.getTicketsByAssignTo(email);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<?> updateTicket(@PathVariable String ticketId, @Valid @RequestBody Tickets updatedTicket) {
        return ticketService.updateTicket(ticketId, updatedTicket)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // NEW: Combined search by createdBy & assignTo
    @GetMapping("/search")
    public ResponseEntity<List<Tickets>> searchTickets(
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) String assignTo) {

        List<Tickets> tickets = ticketService.searchTickets(createdBy, assignTo);

        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tickets);
    }
}
