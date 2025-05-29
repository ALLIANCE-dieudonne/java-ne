package com.alliance.model;

import com.alliance.enums.PayslipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payslips", uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "month", "year"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Double houseAmount;

    @Column(nullable = false)
    private Double transportAmount;

    @Column(nullable = false)
    private Double employeeTaxedAmount;

    @Column(nullable = false)
    private Double pensionAmount;

    @Column(nullable = false)
    private Double medicalInsuranceAmount;

    @Column(nullable = false)
    private Double otherTaxedAmount;

    @Column(nullable = false)
    private Double grossSalary;

    @Column(nullable = false)
    private Double netSalary;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayslipStatus status;


}
