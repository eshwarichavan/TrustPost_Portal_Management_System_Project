package com.example.TrustPostApplication.Models.DTOs;

import com.example.TrustPostApplication.Models.Enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDTO {

    private Long userId;
    private String email;
    private Roles role;
    private String message;
    private String token; // JWT token (optional for now)
}
