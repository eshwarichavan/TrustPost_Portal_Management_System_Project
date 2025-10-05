package com.example.TrustPostApplication.Models.DTOs;

import com.example.TrustPostApplication.Models.Enums.CommentStatuses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    private Long commentId;
    private String commentContent;
    private CommentStatuses status;
    private Long postId;
    private Long userId;
}