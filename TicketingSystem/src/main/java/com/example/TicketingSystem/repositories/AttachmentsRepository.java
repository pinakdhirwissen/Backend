
package com.example.TicketingSystem.repositories;

import com.example.TicketingSystem.models.Attachments;
import com.example.TicketingSystem.models.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {

    // Custom query to fetch attachments using ticketId (String)
    @Query("SELECT a FROM Attachments a WHERE a.ticketId.ticketId = :ticketId")
    List<Attachments> findByTicketId(@Param("ticketId") String ticketId);
}




