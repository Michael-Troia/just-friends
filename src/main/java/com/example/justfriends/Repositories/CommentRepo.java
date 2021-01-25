package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository <Comment, Long> {

    Comment findByBody(String body);
    List<Comment> findAllByUserId(long id);
    Comment findById(long id);
}
