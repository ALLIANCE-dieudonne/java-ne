package com.alliance.serviceImpl;

import com.alliance.dtos.EmployeeUpdateDTO;
import com.alliance.exceptions.InvalidInputException;
import com.alliance.exceptions.ResourceNotFoundException;
import com.alliance.model.Employee;
import com.alliance.repository.EmployeeRepository;
import com.alliance.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public Employee updateEmployee(EmployeeUpdateDTO employeeUpdateDTO) {
        // Validate input
        if (employeeUpdateDTO == null) {
            throw new InvalidInputException("Employee update data is required");
        }

        // Validate required fields
        if (employeeUpdateDTO.getEmail() == null || employeeUpdateDTO.getFirstName() == null ||
                employeeUpdateDTO.getLastName() == null || employeeUpdateDTO.getMobile() == null) {
            throw new InvalidInputException("First name, last name, email, and mobile are required");
        }

        // Find existing employee by email
        Employee existingEmployee = employeeRepository.findByEmail(employeeUpdateDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee with email " + employeeUpdateDTO.getEmail() + " not found"));

        // Update employee fields
        existingEmployee.setFirstName(employeeUpdateDTO.getFirstName());
        existingEmployee.setLastName(employeeUpdateDTO.getLastName());
        existingEmployee.setMobile(employeeUpdateDTO.getMobile());
        if (employeeUpdateDTO.getDateOfBirth() != null) {
            existingEmployee.setDateOfBirth(employeeUpdateDTO.getDateOfBirth());
        }
        if (employeeUpdateDTO.getStatus() != null) {
            existingEmployee.setStatus(employeeUpdateDTO.getStatus());
        }

        // Save updated employee
        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(String code) {
        // Validate input
        if (code == null || code.isEmpty()) {
            throw new InvalidInputException("Employee code is required");
        }

        // Check if employee exists
        Employee employee = employeeRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with code " + code + " not found"));

        // Delete employee
        employeeRepository.delete(employee);
    }
    @Override
    public Employee getEmployeeByCode(String code) {
        // Validate input
        if (code == null || code.isEmpty()) {
            throw new InvalidInputException("Employee code is required");
        }

        // Find employee by code
        return employeeRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with code " + code + " not found"));
    }
}
