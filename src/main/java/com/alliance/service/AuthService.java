package com.alliance.service;

import com.alliance.dtos.EmployeeCreateDTO;
import com.alliance.dtos.LoginRequestDTO;
import com.alliance.dtos.LoginResponseDTO;
import com.alliance.model.Employee;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
   Employee login(LoginRequestDTO loginRequestDTO);
   Employee signup(EmployeeCreateDTO employeeCreateDTO);
}
