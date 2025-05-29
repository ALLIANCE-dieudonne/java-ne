package com.alliance.service;

import com.alliance.dtos.DeductionCreateDTO;
import com.alliance.dtos.DeductionResponseDTO;
import com.alliance.dtos.DeductionUpdateDTO;

import java.util.List;

public interface DeductionService {
    DeductionResponseDTO createDeduction(DeductionCreateDTO deductionCreateDTO);
    DeductionResponseDTO updateDeduction(String code, DeductionUpdateDTO deductionUpdateDTO);
    void deleteDeduction(String code);
    DeductionResponseDTO getDeductionByCode(String code);
    List<DeductionResponseDTO> getAllDeductions();
}