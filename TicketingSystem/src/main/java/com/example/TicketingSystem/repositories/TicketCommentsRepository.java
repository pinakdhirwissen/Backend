package com.example.TicketingSystem.repositories;

import com.example.TicketingSystem.models.TicketComments;
import com.example.TicketingSystem.models.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCommentsRepository extends JpaRepository<TicketComments, String> {
    List<TicketComments> findByTicketId(Tickets ticketId);
    Optional<TicketComments> findByCommentId(Long commentId);


}
