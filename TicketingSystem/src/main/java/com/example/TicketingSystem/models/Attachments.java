//package com.example.TicketingSystem.models;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "Uploads_Attachments", indexes = {
//        @Index(name = "idx_ticket_id", columnList = "ticketId")
//})
//@Getter
//@Setter
//@NoArgsConstructor
//public class Attachments {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String ticketId; // Foreign Key to Ticket
//
//    @Column(nullable = false)
//    private String filePath; // Cloudinary URL
//
//    @Column(nullable = false)
//    private String typeOfFile;
//
//    @Column(nullable = false)
//    private String fileName;
//}
