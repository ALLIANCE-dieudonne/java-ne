package com.alliance.repository;

import com.alliance.model.Employee;
import com.alliance.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    Optional<Payslip> findByEmployeeCodeAndMonthAndYear(String employee, Integer month, Integer year);
    List<Payslip> findByMonthAndYear(Integer month, Integer year);
    boolean existsByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
}
