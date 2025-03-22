package com.example.TicketingSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_comments")
@Getter
@Setter
@NoArgsConstructor
public class TicketComments {
    @Id
    @Column(name = "id", length = 50)  // Assuming max length is 50
    private String ticketId;  // Primary key as String

    @NotBlank(message = "Comment is required")
    @Column(name = "comment", length = 1000, nullable = false)
    private String comment;

    @Column(name = "commented_by", length = 100, nullable = false)
    private String commentedBy;

    @Column(name = "commented_date", nullable = false, updatable = false)
    private LocalDateTime commentedDate;

    @Column(name = "edited_date")
    private LocalDateTime editedDate;

    @Column(name = "is_edited", nullable = false)
    private boolean isEdited = false;

    @OneToOne
    @MapsId // Ensures ticket_id is both primary key and foreign key
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id", nullable = false)
    private Tickets ticket;
    @PrePersist
    protected void onCreate() {
        if (this.commentedDate == null) {
            this.commentedDate = LocalDateTime.now();
        }
    }
    @PreUpdate
    protected void onUpdate() {
        if (this.isEdited) {
            this.editedDate = LocalDateTime.now();
        }
    }
}
