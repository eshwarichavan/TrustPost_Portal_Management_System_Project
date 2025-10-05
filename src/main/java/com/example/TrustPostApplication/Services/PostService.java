package com.example.TrustPostApplication.Services;

import com.example.TrustPostApplication.Models.DTOs.PostRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.PostResponseDTO;
import com.example.TrustPostApplication.Models.Entities.Post;
import com.example.TrustPostApplication.Models.Entities.User;
import com.example.TrustPostApplication.Models.Enums.PostStatuses;
import com.example.TrustPostApplication.Repositories.PostRepository;
import com.example.TrustPostApplication.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService; // To get current logged-in user

    //create post
    public PostResponseDTO createPost(PostRequestDTO dto){
        User user=userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found..."));

        Post post=new Post();
        post.setPostTitle(dto.getPostTitle());
        post.setDescription(dto.getDescription());
        post.setUser(user);
        post.setStatus(PostStatuses.PENDING);  //KEEP THIS DEFAULT VALUE

        Post saved=postRepository.save(post);
        return mapToDto(saved);
    }


    //to get all the post
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    //to get all approved posts
    public List<PostResponseDTO> getAllApprovedPosts() {
        List<Post> approvedPosts = postRepository.findByStatus(PostStatuses.APPROVED);

        return approvedPosts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    //get post by id
    public PostResponseDTO getPostById(Long id){
        Post post=postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found..."));
        return mapToDto(post);
    }

    //to make the edits to post after it gets rejeceted
    public PostResponseDTO updatePost(Long postId,PostRequestDTO dto){
        Post existingPost=postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("Post not found..."));

        //If the post is REJECTED/APPROVED allow edit
        if (existingPost.getStatus() == PostStatuses.PENDING) {
            throw new RuntimeException("Cannot edit post while it’s pending approval.");
        }

        existingPost.setPostTitle(dto.getPostTitle());
        existingPost.setDescription(dto.getDescription());

        // Reset status to PENDING (re-submit for approval)
        existingPost.setStatus(PostStatuses.PENDING);

        // Clear previous approvals/flags
        existingPost.setApprovedBy(null);
        existingPost.setFlaggedBy(null);

        Post updatedPost = postRepository.save(existingPost);

        return mapToDto(updatedPost);
    }


    //to delete the post by post id
    public void deleteById(Long id){
        Post post=postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Post not found..."));
        postRepository.delete(post);

    }

    //get posts by current user
    public List<PostResponseDTO> getPostsByCurrentUser() {
        User currentUser = authService.getCurrentUser();
        List<Post> posts = postRepository.findByUser(currentUser);

        return posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    //get all pending posts
    public List<PostResponseDTO> getAllPendingPosts(){
       List<Post> pendingPosts = postRepository.findByStatus(PostStatuses.PENDING);

       return pendingPosts.stream()
               .map(this::mapToDto)
               .collect(Collectors.toList());
    }


    //Approve the post
    public void approvePost(Long postId){
        Post post=postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("Post not found..."));

        User moderator=authService.getCurrentUser();

        post.setStatus(PostStatuses.APPROVED);
        post.setApprovedBy(moderator);

        postRepository.save(post);
    }

    //Rejects/blacklisted the post
    public void rejectPost(Long postId){
       Post post=postRepository.findById(postId)
               .orElseThrow(()-> new RuntimeException("Post not found..."));

       User moderator=authService.getCurrentUser();

       post.setStatus(PostStatuses.REJECTED);
       post.setFlaggedBy(moderator);

       postRepository.save(post);
    }


    private PostResponseDTO mapToDto(Post post) {
        PostResponseDTO postResponseDTO=new PostResponseDTO();

        postResponseDTO.setPostId(post.getPostId());
        postResponseDTO.setPostTitle(post.getPostTitle());
        postResponseDTO.setDescription(post.getDescription());
        postResponseDTO.setStatus(post.getStatus());
        postResponseDTO.setUploadedBy(post.getUser().getEmail());

        //the email of the user who approved or flagged a post only if that user exists
        if (post.getUser() != null) {
            postResponseDTO.setUploadedByEmail(post.getUser().getEmail());
        }
        if (post.getApprovedBy() != null) {
            postResponseDTO.setApprovedByEmail(post.getApprovedBy().getEmail());
        }
        if (post.getFlaggedBy() != null) {
            postResponseDTO.setFlaggedByEmail(post.getFlaggedBy().getEmail());
        }

        return postResponseDTO;
    }



}
