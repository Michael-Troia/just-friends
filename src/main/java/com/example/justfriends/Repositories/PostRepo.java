package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository <Post, Long> {
}
