package com.alliance.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data

public class DeductionUpdateDTO {
    @NotBlank(message = "Deduction name is required")
    private String deductionName;

    @NotNull(message = "Percentage is required")
    @PositiveOrZero(message = "Percentage must be non-negative")
    private Double percentage;

}