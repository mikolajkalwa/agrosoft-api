package com.agrosoft.restAPI.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long machine_id;
    private String brand;
    private String model;
    private Double monthly_instalment;
    @ManyToOne
    @JoinColumn(name = "farm_id")
    @JsonManagedReference
    private Farm farm;

}
