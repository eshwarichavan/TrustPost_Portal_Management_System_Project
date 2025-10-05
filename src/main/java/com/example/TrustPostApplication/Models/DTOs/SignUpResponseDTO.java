package com.example.TrustPostApplication.Models.DTOs;

import com.example.TrustPostApplication.Models.Enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDTO {


    private Long userId;
    //private String username;
    private String email;
    private Roles role;
    private String message;
    private String token;


}
