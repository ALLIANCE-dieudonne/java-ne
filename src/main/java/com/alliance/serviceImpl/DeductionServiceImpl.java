package com.alliance.serviceImpl;

import com.alliance.dtos.DeductionCreateDTO;
import com.alliance.dtos.DeductionResponseDTO;
import com.alliance.dtos.DeductionUpdateDTO;
import com.alliance.exceptions.InvalidInputException;
import com.alliance.exceptions.ResourceNotFoundException;
import com.alliance.model.Deductions;
import com.alliance.repository.DeductionsRepository;
import com.alliance.service.DeductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeductionServiceImpl implements DeductionService {

    private final DeductionsRepository deductionsRepository;

    @Autowired
    public DeductionServiceImpl(DeductionsRepository deductionsRepository) {
        this.deductionsRepository = deductionsRepository;
    }

    @Override
    public DeductionResponseDTO createDeduction(DeductionCreateDTO deductionCreateDTO) {
        // Validate input
        if (deductionCreateDTO == null || deductionCreateDTO.getDeductionName() == null || deductionCreateDTO.getPercentage() == null) {
            throw new InvalidInputException("Deduction name and percentage are required");
        }

        // Check if deduction name already exists
        if (deductionsRepository.findByDeductionName(deductionCreateDTO.getDeductionName()).isPresent()) {
            throw new InvalidInputException("Deduction with name " + deductionCreateDTO.getDeductionName() + " already exists");
        }

        // Create new deduction
        Deductions deduction = new Deductions();
        deduction.setCode(UUID.randomUUID().toString());
        deduction.setDeductionName(deductionCreateDTO.getDeductionName());
        deduction.setPercentage(deductionCreateDTO.getPercentage());

        // Save to database
        Deductions savedDeduction = deductionsRepository.save(deduction);

        // Map to response DTO
        return mapToResponseDTO(savedDeduction);
    }

    @Override
    public DeductionResponseDTO updateDeduction(String code, DeductionUpdateDTO deductionUpdateDTO) {
        // Validate input
        if (code == null || code.isEmpty()) {
            throw new InvalidInputException("Deduction code is required");
        }
        if (deductionUpdateDTO == null || deductionUpdateDTO.getDeductionName() == null || deductionUpdateDTO.getPercentage() == null) {
            throw new InvalidInputException("Deduction name and percentage are required");
        }

        // Find existing deduction
        Deductions deduction = deductionsRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction with code " + code + " not found"));

        // Check if new name is unique (if changed)
        if (!deduction.getDeductionName().equals(deductionUpdateDTO.getDeductionName()) &&
                deductionsRepository.findByDeductionName(deductionUpdateDTO.getDeductionName()).isPresent()) {
            throw new InvalidInputException("Deduction with name " + deductionUpdateDTO.getDeductionName() + " already exists");
        }

        // Update deduction
        deduction.setDeductionName(deductionUpdateDTO.getDeductionName());
        deduction.setPercentage(deductionUpdateDTO.getPercentage());

        // Save updated deduction
        Deductions updatedDeduction = deductionsRepository.save(deduction);

        // Map to response DTO
        return mapToResponseDTO(updatedDeduction);
    }

    @Override
    public void deleteDeduction(String code) {
        // Validate input
        if (code == null || code.isEmpty()) {
            throw new InvalidInputException("Deduction code is required");
        }

        // Find deduction
        Deductions deduction = deductionsRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction with code " + code + " not found"));

        // Delete deduction
        deductionsRepository.delete(deduction);
    }

    @Override
    public DeductionResponseDTO getDeductionByCode(String code) {
        // Validate input
        if (code == null || code.isEmpty()) {
            throw new InvalidInputException("Deduction code is required");
        }

        // Find deduction
        Deductions deduction = deductionsRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction with code " + code + " not found"));

        // Map to response DTO
        return mapToResponseDTO(deduction);
    }

    @Override
    public List<DeductionResponseDTO> getAllDeductions() {
        // Retrieve all deductions
        List<Deductions> deductions = deductionsRepository.findAll();

        // Map to response DTOs
        return deductions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private DeductionResponseDTO mapToResponseDTO(Deductions deduction) {
        DeductionResponseDTO responseDTO = new DeductionResponseDTO();
        responseDTO.setCode(deduction.getCode());
        responseDTO.setDeductionName(deduction.getDeductionName());
        responseDTO.setPercentage(deduction.getPercentage());
        return responseDTO;
    }
}