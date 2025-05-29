package com.alliance.serviceImpl;

import com.alliance.dtos.EmployeeCreateDTO;
import com.alliance.dtos.LoginRequestDTO;
import com.alliance.dtos.LoginResponseDTO;
import com.alliance.enums.EmployeeStatus;
import com.alliance.enums.UserRole;
import com.alliance.exceptions.InvalidInputException;
import com.alliance.exceptions.ResourceNotFoundException;
import com.alliance.exceptions.UnauthorizedAccessException;
import com.alliance.model.Employee;
import com.alliance.repository.EmployeeRepository;
import com.alliance.service.AuthService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public Employee login(LoginRequestDTO loginRequestDTO) {
        // Validate input
        if (StringUtils.isBlank(loginRequestDTO.getEmail()) || StringUtils.isBlank(loginRequestDTO.getPassword())) {
            throw new InvalidInputException("Email and password are required");
        }

        // Find employee by email
        Employee employee = employeeRepository.findByEmail(loginRequestDTO.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        // Check account status
        if (!employee.isEnabled()) {
            throw new UnauthorizedAccessException("Account is not active");
        }

        // Authenticate credentials

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );


        return employee;
    }
    @Transactional
    @Override
    public Employee signup(EmployeeCreateDTO employeeCreateDTO) {
        log.debug("Attempting to register employee with email: {}", employeeCreateDTO.getEmail());

        // Check if email exists with more logging
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employeeCreateDTO.getEmail());
        if (existingEmployee.isPresent()) {
            log.error("Registration failed - email already exists: {}", employeeCreateDTO.getEmail());
            throw new InvalidInputException("Email " + employeeCreateDTO.getEmail() + " is already registered");
        }
        log.debug("Email verification passed - proceeding with employee creation");


        // Create new employee
        Employee employee = new Employee();
        employee.setCode(UUID.randomUUID().toString());
        employee.setFirstName(employeeCreateDTO.getFirstName());
        employee.setLastName(employeeCreateDTO.getLastName());
        employee.setEmail(employeeCreateDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(employeeCreateDTO.getPassword()));
        employee.setMobile(employeeCreateDTO.getMobile());
        employee.setDateOfBirth(employeeCreateDTO.getDateOfBirth());
        employee.setStatus(EmployeeStatus.ACTIVE);

        // Set roles based on email
        List<String> roles = "fils@gmail.com".equalsIgnoreCase(employeeCreateDTO.getEmail())
                ? List.of(UserRole.ROLE_ADMIN.name(), UserRole.ROLE_MANAGER.name(), UserRole.ROLE_EMPLOYEE.name())
                : List.of(UserRole.ROLE_EMPLOYEE.name());
        System.out.println("Setting roles: " + roles); // Debug log
        employee.setRoles(roles);

        // Save and verify
        Employee savedEmployee = employeeRepository.save(employee);
        System.out.println("Saved roles: " + savedEmployee.getRoles()); // Debug log
        return savedEmployee;
    }

}



