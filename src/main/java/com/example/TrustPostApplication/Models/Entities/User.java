package com.example.TrustPostApplication.Models.Entities;

import com.example.TrustPostApplication.Models.Enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long userId;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="created_At")
    private LocalDateTime createdAt= LocalDateTime.now();

    //injecting Enums
    @Enumerated(EnumType.STRING)
    @Column(name="role" ,nullable = false)
    private Roles role;

    //Adding JPA Mappings

    //One user can add/have multiple posts
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Post> posts;

    //One User can add multiple comments
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Comment> comments;


}
