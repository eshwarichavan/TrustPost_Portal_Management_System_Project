package com.example.TrustPostApplication.Models.Entities;

import com.example.TrustPostApplication.Models.Enums.PostStatuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long postId;

    @Column(name="post_Title")
    private String postTitle;

    @Column(name="description")
    private String description;

    //creating status column
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatuses status = PostStatuses.PENDING;


    //Adding JPA Mappings

    //Many posts belongs to one user only
    @ManyToOne
    @JoinColumn(name="user_id")  //user_id is the Foreign key from user table
    private User user;

    //one post can have multiple comments
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    //which moderator has flagged the post
    //Multiple posts can be flagged by one user(moderator)
    @ManyToOne
    @JoinColumn(name="flagged_by")
    private User flaggedBy;


    //many posts can be appproved by one user(moderator),mmkj
    @ManyToOne
    @JoinColumn(name="approved_by")
    private User approvedBy;

}
