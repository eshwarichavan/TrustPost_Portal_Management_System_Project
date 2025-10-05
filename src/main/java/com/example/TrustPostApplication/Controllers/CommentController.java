package com.example.TrustPostApplication.Controllers;

import com.example.TrustPostApplication.Models.DTOs.CommentRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.CommentResponseDTO;
import com.example.TrustPostApplication.Services.CommentService;
import com.example.TrustPostApplication.Utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comments")
@SecurityRequirement(name = "bearerAuth") // tells Swagger this endpoint requires auth & adds a lock logo in the swagger documentation
@Tag(name = "Comments API", description = "Operations related to comments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
    @Operation(summary = "Add a new comment")
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentRequestDTO dto) {
        return ResponseEntity.ok(commentService.addComment(dto));
    }

    @GetMapping
    @Operation(summary = "Get all comments")
    public ResponseEntity<List<CommentResponseDTO>> getAll() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/getCommentById")
    @Operation(summary = "Get comment by ID")
    public ResponseEntity<CommentResponseDTO> getById(@RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.ok(commentService.getCommentById(commentRequestDTO.getPostId()));
    }


    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
    @Operation(summary = "Update an existing comment")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @RequestBody CommentRequestDTO dto,
            @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = tokenHeader.replace("Bearer ", "");
        String email = JwtUtil.getEmailFromToken(token);

        CommentResponseDTO updated = commentService.updateComment(dto.getPostId(), dto, email);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "Delete comment by ID")
    public ResponseEntity<String> delete(@RequestBody CommentRequestDTO commentRequestDTO) {
        commentService.deleteComment(commentRequestDTO.getPostId());
        return ResponseEntity.ok("Deleted successfully.");
    }


}
