package com.alliance.repository;

import com.alliance.enums.EmployeeStatus;
import com.alliance.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByStatus(EmployeeStatus status);
    Optional<Employee> findByCode(String code);
}
