package com.example.TicketingSystem.controllers;

import com.example.TicketingSystem.dto.CommentDto;
import com.example.TicketingSystem.models.TicketComments;
import com.example.TicketingSystem.services.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{ticketId}/comments")
public class TicketCommentController {

    @Autowired
    private TicketCommentService ticketCommentService;

    @PostMapping
    public ResponseEntity<TicketComments> addComment(@PathVariable String ticketId, @RequestBody CommentDto commentDto) {
        TicketComments comment = ticketCommentService.addComment(ticketId, commentDto);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<TicketComments>> getCommentsByTicketId(@PathVariable String ticketId) {
        List<TicketComments> comments = ticketCommentService.getCommentsByTicketId(ticketId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<TicketComments> editComment(@PathVariable String commentId, @RequestBody CommentDto commentDto) {
        TicketComments updatedComment = ticketCommentService.editComment(commentId, commentDto.getComment(), commentDto.getCommentedBy());
        return ResponseEntity.ok(updatedComment);
    }
}