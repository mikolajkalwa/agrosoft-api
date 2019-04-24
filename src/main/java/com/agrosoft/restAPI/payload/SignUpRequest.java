package com.agrosoft.restAPI.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpRequest {
    @NotEmpty
    private String first_name;
    @NotEmpty
    private String last_name;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}