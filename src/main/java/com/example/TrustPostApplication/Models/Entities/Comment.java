package com.example.TrustPostApplication.Models.Entities;

import com.example.TrustPostApplication.Models.Enums.CommentStatuses;
import com.example.TrustPostApplication.Models.Enums.PostStatuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long CommentId;

    @Column(name="comment_content")
    private String commentContent;

    @Enumerated(EnumType.STRING)
    private CommentStatuses commentStatuses;

    //creating status column
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentStatuses status = CommentStatuses.PENDING;


    //Adding JPA Mappings here

    //many comments belongs to one post
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    //many comments can be written by one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //which moderator has flagged the post
    //Multiple comments can be flagged by one user(moderator)
    @ManyToOne
    @JoinColumn(name="flagged_by")
    private User flaggedBy;

    //multiple comments can be approved by one user(moderator)
    @ManyToOne
    @JoinColumn(name="approved_by")
    private User approvedBy;
}
