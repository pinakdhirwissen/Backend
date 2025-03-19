package com.example.TicketingSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Tickets")
@Getter
@Setter
@NoArgsConstructor
public class Tickets {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 100, nullable = false, updatable = false)
    private String id;

    @Column(name = "request_id", length = 50, nullable = false, updatable = false, unique = true)
    private String requestId;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 50)
    @Column(name = "department", length = 50)
    private String department;

    @Size(max = 100)
    @Column(name = "assign_to", length = 100)
    private String assignTo;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Size(max = 100)
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Size(max = 30)
    @Column(name = "priority", length = 30)
    private String priority;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    // Automatically set the creation date and requestId when persisting
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.requestId == null || this.requestId.isBlank()) {
            this.requestId = generateRequestId();
        }
        System.out.println("Generated Request ID: " + this.requestId); // Debugging log
    }

    // Automatically update the updatedDate when merging
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    private String generateRequestId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "REQ_" + LocalDateTime.now().format(formatter);
    }
}
