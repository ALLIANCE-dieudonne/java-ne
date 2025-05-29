package com.alliance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long expiresIn;
}
