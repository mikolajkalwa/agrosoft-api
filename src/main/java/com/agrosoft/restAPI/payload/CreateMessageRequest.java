package com.agrosoft.restAPI.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateMessageRequest {
    @NotNull
    private Long recipient_id;
    @NotEmpty
    private String subject;
    @NotEmpty
    private String body;
}
