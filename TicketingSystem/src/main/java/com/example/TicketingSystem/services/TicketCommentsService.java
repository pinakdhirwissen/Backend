package com.example.TicketingSystem.services;

import com.example.TicketingSystem.models.TicketComments;
import com.example.TicketingSystem.repositories.TicketCommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketCommentsService {

    @Autowired
    private TicketCommentsRepository commentsRepository;

    public TicketComments saveComment(TicketComments comment) {
        return commentsRepository.save(comment);
    }

    public Optional<TicketComments> updateComment(String id, TicketComments updatedComment) {
        return commentsRepository.findById(id).map(comment -> {
            comment.setComments(updatedComment.getComments());
            comment.setUpdatedBy(updatedComment.getUpdatedBy());
            comment.setUpdatedDate(LocalDateTime.now());
            return commentsRepository.save(comment);
        });
    }


    public List<TicketComments> getCommentsByTicketId(String ticketId) {
        return commentsRepository.findByTicket_Id(ticketId);
    }
}
