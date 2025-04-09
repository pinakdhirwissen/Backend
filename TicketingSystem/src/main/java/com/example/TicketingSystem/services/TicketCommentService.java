package com.example.TicketingSystem.services;

import com.example.TicketingSystem.dto.CommentDto;
import com.example.TicketingSystem.models.TicketComments;
import com.example.TicketingSystem.models.Tickets;
import com.example.TicketingSystem.repositories.TicketCommentsRepository;
import com.example.TicketingSystem.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketCommentService {

    @Autowired
    private TicketCommentsRepository ticketCommentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private Email3Service emailService;
    public List<TicketComments> getCommentsByTicket(Tickets ticketId) {
        return ticketCommentRepository.findByTicketId(ticketId);
    }
//    public String getTicketIdFromCommentId(String commentId) {
//        return ticketCommentRepository.findById(commentId)
//                .map(TicketComments::getTicketId) // Assuming Comment entity has getTicketId()
//                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
//    }

    public TicketComments addComment(String ticketId, CommentDto commentDto) {
        Optional<Tickets> optionalTicket = ticketRepository.findByTicketId(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new IllegalArgumentException("Ticket not found");
        }

        Tickets ticket = optionalTicket.get();
        TicketComments newComment = new TicketComments();
        newComment.setTicketId(ticket);
        newComment.setComment(commentDto.getComment());
        newComment.setCommentedBy(commentDto.getCommentedBy());
        newComment.setCommentedDate(LocalDateTime.now());

        TicketComments savedComment = ticketCommentRepository.save(newComment);

        // ✅ Send email notification when a new comment is added
        emailService.sendCommentEmail(ticket.getCreatedBy(), ticketId, savedComment.getCommentId(),
                savedComment.getComment(), savedComment.getCommentedBy(), false);

        return savedComment;
    }
//    public List<TicketComments> getCommentsByTicketId(String ticketId) {
//        return ticketCommentRepository.findByTicketId(ticketId);
//    }

    public TicketComments editComment(String commentId, String newComment, String user) {
        Optional<TicketComments> optionalComment = ticketCommentRepository.findByCommentId(Long.parseLong(commentId));
        if (optionalComment.isEmpty()) {
            throw new IllegalArgumentException("Comment not found");
        }

        TicketComments comment = optionalComment.get();
        if (!comment.getCommentedBy().equals(user)) {
            throw new IllegalArgumentException("You can only edit your own comments");
        }

        if (comment.getCommentedDate().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Comment can only be edited within 15 minutes");
        }

        comment.setComment(newComment);
        comment.setEdited(true);
        comment.setEditedDate(LocalDateTime.now());
        TicketComments updatedComment = ticketCommentRepository.save(comment);

        // ✅ Send email notification when a comment is edited
        emailService.sendCommentEmail(comment.getTicketId().getCreatedBy(), comment.getTicketId().getTicketId(),
                updatedComment.getCommentId(), updatedComment.getComment(), updatedComment.getCommentedBy(), true);

        return updatedComment;
    }
}
