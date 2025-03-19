//package com.example.TicketingSystem.controllers;
//
//import com.example.TicketingSystem.models.Attachments;
//import com.example.TicketingSystem.services.AttachmentsService;
//import com.example.TicketingSystem.services.CloudinaryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/attachments")
//public class AttachmentsController {
//
//    @Autowired
//    private CloudinaryService cloudinaryService;
//
//    @Autowired
//    private AttachmentsService attachmentsService; // ✅ Use service instead of repository
//
//    // Upload file and save attachment
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
//                                        @RequestParam("ticketId") String ticketId) {
//        try {
//            String fileUrl = cloudinaryService.uploadFile(file);
//            Attachments attachment = new Attachments();
//            attachment.setTicketId(ticketId);
//            attachment.setFilePath(fileUrl);
//            attachment.setTypeOfFile(file.getContentType());
//            attachment.setFileName(file.getOriginalFilename());
//
//            Attachments savedAttachment = attachmentsService.saveAttachment(attachment); // ✅ Call service method
//            return ResponseEntity.ok(savedAttachment);
//        } catch (IOException e) {
//            return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
//        }
//    }
//
//    // Get all attachments by ticket ID id(long)
//    @GetMapping("/by-ticketid/{ticketId}")
//    public ResponseEntity<List<Attachments>> getAttachmentsByTicketId(@PathVariable String ticketId) {
//        List<Attachments> attachments = attachmentsService.getAttachmentsByTicketId(ticketId); // ✅ Call service
//        return ResponseEntity.ok(attachments);
//    }
//
//}
