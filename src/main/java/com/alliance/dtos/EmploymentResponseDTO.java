package com.alliance.dtos;

import com.alliance.enums.EmploymentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data

public class EmploymentResponseDTO {
    private String code;
    private String employeeId;
    private String department;
    private String position;
    private Double baseSalary;
    private EmploymentStatus status;
    private LocalDate joiningDate;


}