package com.alliance.controllers;

import com.alliance.dtos.DeductionCreateDTO;
import com.alliance.dtos.DeductionResponseDTO;
import com.alliance.dtos.DeductionUpdateDTO;
import com.alliance.service.DeductionService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deductions")
@Tag(name = "Deduction Management", description = "APIs for managing deductions and taxes")
@SecurityRequirement(name = "bearer-jwt")

public class DeductionController {

    private final DeductionService deductionService;

    @Autowired
    public DeductionController(DeductionService deductionService) {
        this.deductionService = deductionService;
    }

    @PostMapping
    @Operation(summary = "Create a new deduction", description = "Creates a new deduction type (e.g., tax, pension). Requires ROLE_MANAGER authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deduction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<DeductionResponseDTO> createDeduction(@Valid @RequestBody DeductionCreateDTO deductionCreateDTO) {
        DeductionResponseDTO responseDTO = deductionService.createDeduction(deductionCreateDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    @Operation(summary = "Update a deduction", description = "Updates an existing deduction by its code. Requires ROLE_MANAGER authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Deduction not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<DeductionResponseDTO> updateDeduction(
            @PathVariable("code") String code,
            @Valid @RequestBody DeductionUpdateDTO deductionUpdateDTO) {
        DeductionResponseDTO responseDTO = deductionService.updateDeduction(code, deductionUpdateDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    @Operation(summary = "Delete a deduction", description = "Deletes a deduction by its code. Requires ROLE_MANAGER authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deduction deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid deduction code"),
            @ApiResponse(responseCode = "404", description = "Deduction not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Void> deleteDeduction(@PathVariable("code") String code) {
        deductionService.deleteDeduction(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get deduction details", description = "Retrieves details of a deduction by its code. Requires ROLE_MANAGER authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid deduction code"),
            @ApiResponse(responseCode = "404", description = "Deduction not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<DeductionResponseDTO> getDeduction(@PathVariable("code") String code) {
        DeductionResponseDTO responseDTO = deductionService.getDeductionByCode(code);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all deductions", description = "Retrieves a list of all deductions. Requires ROLE_MANAGER authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of deductions retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<List<DeductionResponseDTO>> getAllDeductions() {
        List<DeductionResponseDTO> responseDTOs = deductionService.getAllDeductions();
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }
}