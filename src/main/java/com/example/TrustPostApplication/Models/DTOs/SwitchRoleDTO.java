package com.example.TrustPostApplication.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitchRoleDTO {

    private String targetRole;  // e.g., "MODERATOR" or "USER"

}
