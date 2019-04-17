package com.agrosoft.restAPI.payload;

import lombok.Data;

@Data
public class SignUpRequest {
    private String first_name;
    private String last_name;
    private String username;
    private String password;
}