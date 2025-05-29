package com.alliance.dtos;

import lombok.Data;

@Data
public class MessageResponseDTO {
    private Long id;
    private String employeeId;
    private String message;
    private String monthYear;


}