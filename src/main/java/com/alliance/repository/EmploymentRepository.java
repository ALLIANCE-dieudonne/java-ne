package com.alliance.repository;

import com.alliance.enums.EmploymentStatus;
import com.alliance.model.Employee;
import com.alliance.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmploymentRepository extends JpaRepository<Employment, String> {
    Optional<Employment> findByEmployeeAndStatus(Employee employee, EmploymentStatus status);
}
