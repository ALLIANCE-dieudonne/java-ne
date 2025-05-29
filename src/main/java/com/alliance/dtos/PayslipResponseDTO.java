package com.alliance.dtos;

import com.alliance.enums.PayslipStatus;
import lombok.Data;

@Data
public class PayslipResponseDTO {
    private Long id;
    private String employeeId;
    private Double houseAmount;
    private Double transportAmount;
    private Double employeeTaxedAmount;
    private Double pensionAmount;
    private Double medicalInsuranceAmount;
    private Double otherTaxedAmount;
    private Double grossSalary;
    private Double netSalary;
    private Integer month;
    private Integer year;
    private PayslipStatus status;


}