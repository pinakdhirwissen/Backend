package com.example.TicketingSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCommentRequest {
    private String newComment;
    private String user;
}
