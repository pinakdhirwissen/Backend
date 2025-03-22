package com.example.TicketingSystem.services;

import com.example.TicketingSystem.dto.CommentDto;
import com.example.TicketingSystem.models.TicketComments;

import java.util.List;

public interface TicketCommentService {
    TicketComments addComment(String ticketId, CommentDto commentDto);
    List<TicketComments> getCommentsByTicketId(String ticketId);
    TicketComments editComment(String commentId, String newComment, String user);
}
