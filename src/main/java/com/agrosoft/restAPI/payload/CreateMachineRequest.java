package com.agrosoft.restAPI.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateMachineRequest {
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
    private Double monthly_instalment;
}
