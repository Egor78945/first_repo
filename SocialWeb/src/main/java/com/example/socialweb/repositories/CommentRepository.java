package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findCommentById(Long id);
    void deleteAllByNewsId(Long id);
}
