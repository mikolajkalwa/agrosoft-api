package com.agrosoft.restAPI.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateFieldRequest {
    @NotNull
    private Double area;
    private String crop;
    private String status;
    @NotNull
    private Long farm_id;
}
