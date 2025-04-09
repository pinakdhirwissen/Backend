package com.example.TicketingSystem.services;

import com.example.TicketingSystem.models.Attachments;
import com.example.TicketingSystem.models.Tickets;
import com.example.TicketingSystem.repositories.AttachmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentsService {

    @Autowired
    private AttachmentsRepository attachmentsRepository;

    // Save attachment
    public Attachments saveAttachment(Attachments attachment) {
        return attachmentsRepository.save(attachment);
    }

    // Fetch all attachments by ticket id
    public List<Attachments> getAttachmentsByTicketId(Tickets ticketId) {

        return attachmentsRepository.findByTicketId(ticketId.getTicketId());
    }

}
