package com.example.TrustPostApplication.Models.DTOs;

import com.example.TrustPostApplication.Models.Enums.PostStatuses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private Long postId;
    private String postTitle;
    private String description;
    private String uploadedBy;
    private PostStatuses status;
    private String flaggedBy; // nullable
    private String approvedBy;

    public void setUploadedByEmail(String email) {
    }

    public void setApprovedByEmail(String email) {
    }

    public void setFlaggedByEmail(String email) {

    }
}
