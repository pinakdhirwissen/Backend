package com.example.TicketingSystem.repositories;

import com.example.TicketingSystem.models.Tickets;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Tickets, String> {
    int countByAssignTo(String assignTo);

    List<Tickets> findByStatusAndCreatedDateBeforeAndAssignToIsNull(String status, LocalDateTime createdDate);

    List<Tickets> findByStatus(String status);

    List<Tickets> findByCreatedBy(String createdBy);

    List<Tickets> findByAssignTo(String assignTo);

    Optional<Tickets> findByTicketId(String ticketId);

    @Query("SELECT t.createdBy FROM Tickets t WHERE t.ticketId = :ticketId")
    Optional<String> findEmailByTicketId(@Param("ticketId") String ticketId);

    @Query("SELECT t FROM Tickets t " +
            "WHERE (:createdBy IS NULL OR t.createdBy = :createdBy) " +
            "OR (:assignTo IS NULL OR t.assignTo = :assignTo)")
    List<Tickets> findByCreatedByOrAssignTo(@Param("createdBy") String createdBy,
                                            @Param("assignTo") String assignTo);

    List<Tickets> getTicketsByDepartment(@Size(max = 50) String department);

    List<Tickets> findByDepartment(@Size(max = 50) String department);

    @Modifying
    @Transactional
    @Query("UPDATE Tickets t SET t.assignTo = :assigneeEmail, t.status = 'Assigned' WHERE t.ticketId = :ticketId")
    int assignTicketToUser(@Param("ticketId") String ticketId, @Param("assigneeEmail") String assigneeEmail);

    List<Tickets> findByAssignToIsNull();
}
