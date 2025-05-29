package com.alliance.controllers;

import com.alliance.dtos.EmployeeCreateDTO;
import com.alliance.dtos.LoginRequestDTO;
import com.alliance.dtos.LoginResponseDTO;
import com.alliance.model.Employee;
import com.alliance.service.AuthService;
import com.alliance.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;



    @Operation(summary = "Register a new employee", description = "Creates a new employee account and returns the registered employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee registered successfully",
                    content = @Content(schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/register")
    public ResponseEntity<Employee> register(@RequestBody EmployeeCreateDTO employeeDTO) {
        Employee registeredEmployee = authService.signup(employeeDTO);
        return ResponseEntity.ok(registeredEmployee);
    }

    @Operation(summary = "Authenticate employee", description = "Authenticates an employee and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginRequestDTO loginDTO) {
        Employee authenticatedEmployee = authService.login(loginDTO);
        String token = jwtService.generateToken(authenticatedEmployee);
        LoginResponseDTO loginResponse = new LoginResponseDTO(token, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}