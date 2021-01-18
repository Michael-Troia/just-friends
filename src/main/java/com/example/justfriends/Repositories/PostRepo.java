package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository <Post, Long> {
    Post findById(long id);
    List<Post> findAllByUserUsername(String username);
}
