package com.agrosoft.restAPI.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Farm {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long farm_id;
    private String address;
    @OneToMany(mappedBy = "farm")
    @JsonBackReference
    private List<User> users;
}
