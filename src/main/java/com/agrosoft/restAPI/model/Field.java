package com.agrosoft.restAPI.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long field_id;
    private Double area;
    private String crop;
    private String status;
    @ManyToOne
    @JoinColumn(name = "farm_id")
    @JsonManagedReference
    private Farm farm;
}
