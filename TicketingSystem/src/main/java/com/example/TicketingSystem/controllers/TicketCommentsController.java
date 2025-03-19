package com.example.TicketingSystem.controllers;

import com.example.TicketingSystem.models.TicketComments;
import com.example.TicketingSystem.services.TicketCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class TicketCommentsController {

    @Autowired
    private TicketCommentsService commentsService;

    @PostMapping
    public TicketComments createComment(@RequestBody TicketComments comment) {
        comment.setCreatedDate(LocalDateTime.now());
        return commentsService.saveComment(comment);
    }

    @PutMapping("/{id}")
    public Optional<TicketComments> updateComment(@PathVariable String id, @RequestBody TicketComments comment) {
        comment.setUpdatedDate(LocalDateTime.now());
        return commentsService.updateComment(id, comment);
    }

    @GetMapping("/ticket/{ticketId}")
    public List<TicketComments> getCommentsByTicketId(@PathVariable String ticketId) {
        return commentsService.getCommentsByTicketId(ticketId);
    }
}
