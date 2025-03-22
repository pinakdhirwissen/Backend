package com.example.TicketingSystem.services;

import com.example.TicketingSystem.dto.CommentDto;
import com.example.TicketingSystem.models.TicketComments;
import com.example.TicketingSystem.repositories.TicketCommentsRepository;
import com.example.TicketingSystem.services.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketCommentServiceImpl implements TicketCommentService {

    @Autowired
    private TicketCommentsRepository ticketCommentsRepository;

    // ✅ Add a comment to a ticket
    @Override
    public TicketComments addComment(String ticketId, CommentDto commentDto) {
        TicketComments comment = new TicketComments();
        comment.setTicketId(ticketId);
        comment.setComment(commentDto.getComment());
        comment.setCommentedBy(commentDto.getCommentedBy());
        comment.setCommentedDate(LocalDateTime.now());
        comment.setEdited(false);

        return ticketCommentsRepository.save(comment);
    }

    // ✅ Get all comments for a ticket
    @Override
    public List<TicketComments> getCommentsByTicketId(String ticketId) {
        return ticketCommentsRepository.findByTicketId(ticketId);
    }

    // ✅ Edit a comment (only within 15 minutes)
    @Override
    public TicketComments editComment(String commentId, String newComment, String user) {
        Optional<TicketComments> optionalComment = ticketCommentsRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new IllegalArgumentException("Comment not found");
        }

        TicketComments comment = optionalComment.get();
        if (!comment.getCommentedBy().equals(user)) {
            throw new IllegalArgumentException("You can only edit your own comments.");
        }

        if (comment.getCommentedDate().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Comment can only be edited within 15 minutes.");
        }

        comment.setComment(newComment);
        comment.setEdited(true);
        comment.setEditedDate(LocalDateTime.now());

        return ticketCommentsRepository.save(comment);
    }
}