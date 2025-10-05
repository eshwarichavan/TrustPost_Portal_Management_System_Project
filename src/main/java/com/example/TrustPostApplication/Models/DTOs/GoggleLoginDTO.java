package com.example.TrustPostApplication.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoggleLoginDTO {
    private String idToken;  // Google's ID token from frontend
}
