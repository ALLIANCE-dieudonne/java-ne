package com.alliance.controllers;

import com.alliance.dtos.EmployeeResponseDTO;
import com.alliance.dtos.EmployeeUpdateDTO;
import com.alliance.exceptions.InvalidInputException;
import com.alliance.exceptions.ResourceNotFoundException;
import com.alliance.exceptions.UnauthorizedAccessException;
import com.alliance.model.Employee;
import com.alliance.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Employee Management", description = "APIs for managing employee details")
@SecurityRequirement(name = "bearer-jwt")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "Update an employee", description = "Updates an existing employee's details. Requires ROLE_MANAGER authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable("code") String code,
            @Valid @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        // Validate code
        if (code == null || code.isEmpty()) {
            throw new InvalidInputException("Employee code is required");
        }

        // Ensure the email in DTO matches an existing employee
        Employee updatedEmployee = employeeService.updateEmployee(employeeUpdateDTO);

        // Map to response DTO
        EmployeeResponseDTO responseDTO = mapToResponseDTO(updatedEmployee);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "Delete an employee", description = "Deletes an employee by their code. Requires ROLE_MANAGER authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee code"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Void> deleteEmployee(@PathVariable("code") String code) {
        employeeService.deleteEmployee(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Get employee details", description = "Retrieves details of an employee by their code. Accessible to ROLE_MANAGER or the employee themselves.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee code"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable("code") String code) {
        // Validate code
        if (code == null || code.isEmpty()) {
            throw new InvalidInputException("Employee code is required");
        }

        // Get authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName(); // Email from JWT

        // Find employee by code
        Employee employee = employeeService.getEmployeeByCode(code);

        // Check if the authenticated user is allowed to view this employee
        if (!authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MANAGER"))
                && !employee.getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedAccessException("You are not authorized to view this employee's details");
        }

        // Map to response DTO
        EmployeeResponseDTO responseDTO = mapToResponseDTO(employee);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setCode(employee.getCode());
        responseDTO.setFirstName(employee.getFirstName());
        responseDTO.setLastName(employee.getLastName());
        responseDTO.setEmail(employee.getEmail());
        responseDTO.setMobile(employee.getMobile());
        responseDTO.setDateOfBirth(employee.getDateOfBirth());
        responseDTO.setStatus(employee.getStatus());
        responseDTO.setRoles(employee.getRoles());
        return responseDTO;
    }
}