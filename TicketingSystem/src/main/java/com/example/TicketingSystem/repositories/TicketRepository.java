package com.example.TicketingSystem.repositories;

import com.example.TicketingSystem.models.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Tickets, String> {
    List<Tickets> findByStatus(String status);
    List<Tickets> findByCreatedBy(String createdBy);
    List<Tickets> findByAssignTo(String assignTo);


    @Query("SELECT t FROM Tickets t " +
            "WHERE (:createdBy IS NULL OR t.createdBy = :createdBy) " +
            "OR (:assignTo IS NULL OR t.assignTo = :assignTo)")
    List<Tickets> findByCreatedByOrAssignTo(@Param("createdBy") String createdBy,
                                            @Param("assignTo") String assignTo);
}
