package com.example.justfriends.Repositories;

import com.example.justfriends.Models.User;
import com.example.justfriends.Models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFriendRepo extends JpaRepository <UserFriend, Long> {
    List<UserFriend> findAllByUserId(long id);
    List<UserFriend> findAllById(long id);
    List<UserFriend> findAllByUserUsername(String username);
    List<UserFriend> findAllByUser(User user);
    UserFriend findById(long id);
}
