package com.alliance.serviceImpl;

import com.alliance.enums.EmployeeStatus;
import com.alliance.enums.EmploymentStatus;
import com.alliance.model.*;
import com.alliance.repository.*;
import com.alliance.service.PayrollService;
import com.alliance.enums.PayslipStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PayrollServiceImpl implements PayrollService {

    private final EmployeeRepository employeeRepository;
    private final EmploymentRepository employmentRepository;
    private final DeductionsRepository deductionsRepository;
    private final PayslipRepository payslipRepository;
    private final MessageRepository messageRepository;

    public PayrollServiceImpl(EmployeeRepository employeeRepository,
                              EmploymentRepository employmentRepository,
                              DeductionsRepository deductionsRepository,
                              PayslipRepository payslipRepository,
                              MessageRepository messageRepository) {
        this.employeeRepository = employeeRepository;
        this.employmentRepository = employmentRepository;
        this.deductionsRepository = deductionsRepository;
        this.payslipRepository = payslipRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public List<Payslip> generatePayroll(Integer month, Integer year) {
        // Check if payroll already exists for this month/year
        if (!payslipRepository.findByMonthAndYear(month, year).isEmpty()) {
            throw new IllegalStateException("Payroll already generated for " + month + "/" + year);
        }

        // Get all active employees
        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

        return activeEmployees.stream()
                .map(employee -> {
                    Employment employment = employmentRepository.findByEmployeeAndStatus(employee, EmploymentStatus.ACTIVE)
                            .orElseThrow(() -> new RuntimeException("No active employment found for employee: " + employee.getCode()));

                    return calculateAndSavePayslip(employee, employment, month, year);
                })
                .toList();
    }

    private Payslip calculateAndSavePayslip(Employee employee, Employment employment, Integer month, Integer year) {
        Double baseSalary = employment.getBaseSalary();

        // Get deduction percentages from database
        Double housingPercentage = deductionsRepository.findById("HOUSING")
                .map(Deductions::getPercentage)
                .orElse(0.14); // Default to 14% if not found

        Double transportPercentage = deductionsRepository.findById("TRANSPORT")
                .map(Deductions::getPercentage)
                .orElse(0.14); // Default to 14% if not found

        Double taxPercentage = deductionsRepository.findById("TAX")
                .map(Deductions::getPercentage)
                .orElse(0.30); // Default to 30% if not found

        Double pensionPercentage = deductionsRepository.findById("PENSION")
                .map(Deductions::getPercentage)
                .orElse(0.06); // Default to 6% if not found

        Double medicalPercentage = deductionsRepository.findById("MEDICAL")
                .map(Deductions::getPercentage)
                .orElse(0.05); // Default to 5% if not found

        Double othersPercentage = deductionsRepository.findById("OTHERS")
                .map(Deductions::getPercentage)
                .orElse(0.05); // Default to 5% if not found

        // Calculate amounts
        Double housingAmount = baseSalary * housingPercentage;
        Double transportAmount = baseSalary * transportPercentage;
        Double grossSalary = baseSalary + housingAmount + transportAmount;

        Double taxAmount = baseSalary * taxPercentage;
        Double pensionAmount = baseSalary * pensionPercentage;
        Double medicalAmount = baseSalary * medicalPercentage;
        Double othersAmount = baseSalary * othersPercentage;

        Double netSalary = grossSalary - (taxAmount + pensionAmount + medicalAmount + othersAmount);

        // Create and save payslip
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setHouseAmount(housingAmount);
        payslip.setTransportAmount(transportAmount);
        payslip.setEmployeeTaxedAmount(taxAmount);
        payslip.setPensionAmount(pensionAmount);
        payslip.setMedicalInsuranceAmount(medicalAmount);
        payslip.setOtherTaxedAmount(othersAmount);
        payslip.setGrossSalary(grossSalary);
        payslip.setNetSalary(netSalary);
        payslip.setMonth(month);
        payslip.setYear(year);
        payslip.setStatus(PayslipStatus.PENDING);

        return payslipRepository.save(payslip);
    }

    @Override
    public Payslip getEmployeePayslip(String employeeId, Integer month, Integer year) {
        return payslipRepository.findByEmployeeCodeAndMonthAndYear(employeeId, month, year)
                .orElseThrow(() -> new RuntimeException("Payslip not found"));
    }

    @Override
    public List<Payslip> getAllPayslips(Integer month, Integer year) {
        return payslipRepository.findByMonthAndYear(month, year);
    }

    @Override
    @Transactional
    public void approvePayroll(Integer month, Integer year) {
        List<Payslip> payslips = payslipRepository.findByMonthAndYear(month, year);

        if (payslips.isEmpty()) {
            throw new RuntimeException("No payroll found for " + month + "/" + year);
        }

        payslips.forEach(payslip -> {
            payslip.setStatus(PayslipStatus.PAID);
            payslipRepository.save(payslip);

            // Create and save message
            Message message = new Message();
            message.setEmployee(payslip.getEmployee());
            message.setMonthYear(month + "/" + year);
            message.setMessage(String.format(
                    "Dear %s, your salary for %d/%d from RCA amounting to %.2f RWF has been credited to your account %s successfully.",
                    payslip.getEmployee().getFirstName(),
                    month,
                    year,
                    payslip.getNetSalary(),
                    payslip.getEmployee().getCode()
            ));

            messageRepository.save(message);

            // TODO: Implement email sending logic here
        });
    }
}