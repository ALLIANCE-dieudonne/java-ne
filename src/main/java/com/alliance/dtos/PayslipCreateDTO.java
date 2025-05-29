package com.alliance.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.YearMonth;

@Data
public class PayslipCreateDTO {
    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    @NotNull(message = "Period is required")
    @PastOrPresent(message = "Period cannot be in the future")
    private YearMonth period;


}