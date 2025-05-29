package com.alliance.service;

import com.alliance.dtos.EmployeeUpdateDTO;
import com.alliance.model.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
Employee updateEmployee(EmployeeUpdateDTO employee);
void deleteEmployee(String code);
Employee getEmployeeByCode(String code);
}
