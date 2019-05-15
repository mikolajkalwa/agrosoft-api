package com.agrosoft.restAPI.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateMachineRequest {
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
    private Double monthly_instalment;
    @NotNull
    private Long farm_id;
}
