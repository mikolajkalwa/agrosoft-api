package com.agrosoft.restAPI.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String first_name;
    private String surname;
    private String email;
    private String password_hash;
    private int privileges;
    private Date created_at;
    private Date updated_at;
    private Date last_login;
    @ManyToOne
    @JoinColumn(name="farm_id")
    @JsonManagedReference
    private Farm farm;
}
