package com.alliance.dtos;

import lombok.Data;

@Data
public class DeductionResponseDTO {
    private String code;
    private String deductionName;
    private Double percentage;

}