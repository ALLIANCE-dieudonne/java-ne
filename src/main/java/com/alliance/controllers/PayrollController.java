package com.alliance.controllers;

import com.alliance.model.Payslip;
import com.alliance.service.PayrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/generate/{month}/{year}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Payslip>> generatePayroll(
            @PathVariable Integer month,
            @PathVariable Integer year) {
        return ResponseEntity.ok(payrollService.generatePayroll(month, year));
    }

    @GetMapping("/employee/{employeeId}/{month}/{year}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    public ResponseEntity<Payslip> getEmployeePayslip(
            @PathVariable String employeeId,
            @PathVariable Integer month,
            @PathVariable Integer year) {
        return ResponseEntity.ok(payrollService.getEmployeePayslip(employeeId, month, year));
    }

    @GetMapping("/all/{month}/{year}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Payslip>> getAllPayslips(
            @PathVariable Integer month,
            @PathVariable Integer year) {
        return ResponseEntity.ok(payrollService.getAllPayslips(month, year));
    }

    @PutMapping("/approve/{month}/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approvePayroll(
            @PathVariable Integer month,
            @PathVariable Integer year) {
        payrollService.approvePayroll(month, year);
        return ResponseEntity.ok().build();
    }
}