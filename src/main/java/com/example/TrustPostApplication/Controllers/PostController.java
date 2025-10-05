package com.example.TrustPostApplication.Controllers;

import com.example.TrustPostApplication.Models.DTOs.PostRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.PostResponseDTO;
import com.example.TrustPostApplication.Services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
@SecurityRequirement(name = "bearerAuth") // tells Swagger this endpoint requires auth & adds a lock logo in the swagger documentation
@Tag(name = "Posts API", description = "Operations related to posts")
public class PostController {

    @Autowired
    private PostService postService;


    //to create the posts
    @PreAuthorize("permitAll()")
    @PostMapping("/create")
    @Operation(summary = "To create the posts")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO dto){
        return ResponseEntity.ok(postService.createPost(dto));
    }


    //get all posts
    @GetMapping
    @Operation(summary = "To get all posts")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }


    //get posts by id
    @GetMapping("/getpostbyid")
    @Operation(summary = "Retrieve post by ID")
    public ResponseEntity<PostResponseDTO> getPostById(@RequestBody PostRequestDTO postRequestDTO){
        return ResponseEntity.ok(postService.getPostById(postRequestDTO.getUserId()));
    }


    //for editing the post contains
    @PutMapping("/edit")
    @Operation(summary = "Update an existing post")
    public ResponseEntity<PostResponseDTO> updatePost(
            @RequestBody PostRequestDTO dto)
    {
        PostResponseDTO updated=postService.updatePost(dto.getUserId(),dto);
        return ResponseEntity.ok(updated);
    }


    //delete the post by id
    @DeleteMapping("/delete")
    @Operation(summary = "Delete post by ID")
    public ResponseEntity<String> deletePost(@RequestBody Long id){
        postService.deleteById(id);
        return ResponseEntity.ok("Post Deleted Successfully...");
    }


    @GetMapping("/myposts")
    @Operation(summary = "Retrives all user posts")
    public ResponseEntity<List<PostResponseDTO>> getMyPosts() {
        List<PostResponseDTO> myPosts = postService.getPostsByCurrentUser();
        return ResponseEntity.ok(myPosts);
    }


    //get all approved post
    @GetMapping("/approvedpost")
    @Operation(summary = "Retrieves All approved posts")
    public ResponseEntity<List<PostResponseDTO>> getAllApprovedPosts(){
        List<PostResponseDTO> posts=postService.getAllApprovedPosts();
        return ResponseEntity.ok(posts);
    }


    //get pending posts
    @GetMapping("/pending")
    @Operation(summary = "Retrieves All pending posts")
    public ResponseEntity<List<PostResponseDTO>> getPendingPosts(){
        List<PostResponseDTO> posts=postService.getAllPendingPosts();
        return ResponseEntity.ok(posts);
    }


    //moderator approved posts
    @GetMapping("/approve")
    @Operation(summary = "Retrieve Moderator approved posts")
    public ResponseEntity<String> approvePost(@RequestBody PostRequestDTO postRequestDTO){
        postService.approvePost(postRequestDTO.getUserId());
        return ResponseEntity.ok("Post Approved by Moderator...");
    }


    //moderator rejected posts
    @GetMapping("/reject")
    @Operation(summary = "Retrieve Moderator Rejected posts")
    public ResponseEntity<String> rejectPost(@RequestBody PostRequestDTO postRequestDTO){
        postService.rejectPost(postRequestDTO.getUserId());
        return ResponseEntity.ok("Post Rejected by Moderator...");
    }

}
