package com.example.TicketingSystem.services;

import com.example.TicketingSystem.models.Tickets;
import com.example.TicketingSystem.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

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

    // Combined search (optional filters)
    public List<Tickets> searchTickets(String createdBy, String assignTo) {
        return ticketRepository.findByCreatedByOrAssignTo(createdBy, assignTo);
    }
}
