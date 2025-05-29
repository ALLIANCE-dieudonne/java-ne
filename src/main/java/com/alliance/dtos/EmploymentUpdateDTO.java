package com.alliance.dtos;

import com.alliance.enums.EmploymentStatus;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmploymentUpdateDTO {
    private String department;
    private String position;
    @Positive(message = "Base salary must be positive")
    private Double baseSalary;
    private EmploymentStatus status;
    private LocalDate joiningDate;

}