package com.alliance.service;

import com.alliance.model.Payslip;
import java.util.List;

public interface PayrollService {
    List<Payslip> generatePayroll(Integer month, Integer year);
    Payslip getEmployeePayslip(String employeeId, Integer month, Integer year);
    List<Payslip> getAllPayslips(Integer month, Integer year);
    void approvePayroll(Integer month, Integer year);
}