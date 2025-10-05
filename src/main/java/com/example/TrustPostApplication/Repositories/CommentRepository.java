package com.example.TrustPostApplication.Repositories;

import com.example.TrustPostApplication.Models.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
