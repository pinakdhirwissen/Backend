package com.example.TicketingSystem.repositories;

import com.example.TicketingSystem.models.TicketComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommentsRepository extends JpaRepository<TicketComments, String> {

    List<TicketComments> findByTicket_Id(String ticketId);
}
