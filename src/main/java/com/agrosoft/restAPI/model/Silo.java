package com.agrosoft.restAPI.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Silo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long silo_id;
    private String content;
    private Integer capacity;
    private Double fill_level;
    @ManyToOne
    @JoinColumn(name = "farm_id")
    @JsonManagedReference
    private Farm farm;
}
