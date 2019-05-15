package com.agrosoft.restAPI.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateSiloRequest {
    private String content;
    @NotNull
    private Integer capacity;
    private Double fill_level;
    @NotNull
    private Long farm_id;
}
