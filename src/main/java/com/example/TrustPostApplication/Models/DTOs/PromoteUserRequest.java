package com.example.TrustPostApplication.Models.DTOs;

import com.example.TrustPostApplication.Models.Enums.Roles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoteUserRequest {

    private String email;

    @JsonProperty("role")
    private Roles role;

}
