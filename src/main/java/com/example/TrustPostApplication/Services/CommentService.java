package com.example.TrustPostApplication.Services;

import ch.qos.logback.classic.boolex.MarkerList;
import com.example.TrustPostApplication.Models.DTOs.CommentRequestDTO;
import com.example.TrustPostApplication.Models.DTOs.CommentResponseDTO;
import com.example.TrustPostApplication.Models.Entities.Comment;
import com.example.TrustPostApplication.Models.Entities.Post;
import com.example.TrustPostApplication.Models.Entities.User;
import com.example.TrustPostApplication.Models.Enums.CommentStatuses;
import com.example.TrustPostApplication.Repositories.CommentRepository;
import com.example.TrustPostApplication.Repositories.PostRepository;
import com.example.TrustPostApplication.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    //add a comment
    public CommentResponseDTO addComment(CommentRequestDTO dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setCommentContent(dto.getCommentContent());
        comment.setStatus(CommentStatuses.PENDING);
        comment.setPost(post);
        comment.setUser(user);

        Comment saved = commentRepository.save(comment);

        return new CommentResponseDTO(
                saved.getCommentId(),
                saved.getCommentContent(),
                saved.getStatus(),
                saved.getPost().getPostId(),
                saved.getUser().getUserId()
        );
    }


    //get comment by id
    public CommentResponseDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        return new CommentResponseDTO(
                comment.getCommentId(),
                comment.getCommentContent(),
                comment.getStatus(),
                comment.getPost().getPostId(),
                comment.getUser().getUserId()
        );
    }


    //get all the comment
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(comment ->
                new CommentResponseDTO(
                        comment.getCommentId(),
                        comment.getCommentContent(),
                        comment.getStatus(),
                        comment.getPost().getPostId(),
                        comment.getUser().getUserId()
                )
        ).collect(Collectors.toList());
    }


    //updating the comment
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO dto, String requesterEmail) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getEmail().equals(requesterEmail)) {
            throw new RuntimeException("You can only edit your own comment.");
        }

        if (comment.getStatus() != CommentStatuses.REJECTED) {
            throw new RuntimeException("Only rejected comments can be edited.");
        }

        comment.setCommentContent(dto.getCommentContent());
        comment.setStatus(CommentStatuses.PENDING); // reset status to pending
        Comment updated = commentRepository.save(comment);

        return new CommentResponseDTO(
                updated.getCommentId(),
                updated.getCommentContent(),
                updated.getStatus(),
                updated.getPost().getPostId(),
                updated.getUser().getUserId()
        );

    }

    //delete the comment by id
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        //Get the email of the currently authenticated user
        String requesterEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        //Check if the requester is the comment author
        boolean isAuthor = comment.getUser().getEmail().equals(requesterEmail);

        //Check if the requester has MODERATOR role
        boolean isModerator = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_MODERATOR"));

        //Only allow if author or moderator
        if (!isAuthor && !isModerator) {
            throw new RuntimeException("You are not authorized to delete this comment.");
        }

        commentRepository.deleteById(id);
    }

}
