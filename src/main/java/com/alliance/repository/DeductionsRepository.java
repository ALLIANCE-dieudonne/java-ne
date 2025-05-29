package com.alliance.repository;

import com.alliance.model.Deductions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeductionsRepository extends JpaRepository<Deductions, String> {
    Optional<Deductions> findByDeductionName(String deductionName);
}