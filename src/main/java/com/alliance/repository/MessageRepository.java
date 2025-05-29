package com.alliance.repository;


import com.alliance.model.Employee;
import com.alliance.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEmployeeCode(String employeeCode);
}