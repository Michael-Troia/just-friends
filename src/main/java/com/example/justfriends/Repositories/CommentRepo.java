package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository <Comment, Long> {

}
