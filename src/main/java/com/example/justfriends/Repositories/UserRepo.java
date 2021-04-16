package com.example.justfriends.Repositories;

import com.example.justfriends.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository <User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
