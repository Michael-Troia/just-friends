package com.example.justfriends.Repositories;

import com.example.justfriends.Models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFriendRepo extends JpaRepository <UserFriend, Long> {
    public List<UserFriend> findAllByUserId(long id);
}
