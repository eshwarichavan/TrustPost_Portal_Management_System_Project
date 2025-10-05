package com.example.TrustPostApplication.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {

    private String postTitle;
    private String description;
    private Long userId;
}
