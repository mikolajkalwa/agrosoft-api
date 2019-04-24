package com.agrosoft.restAPI.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long message_id;
    private String subject;
    private String body;
    private Instant created_at;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
}
