package com.example.TrustPostApplication.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
