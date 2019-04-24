package com.agrosoft.restAPI.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class MessageRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean is_read;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
}
