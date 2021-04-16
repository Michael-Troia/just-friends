package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Status;
import com.example.justfriends.Models.User;
import com.example.justfriends.Models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFriendRepo extends JpaRepository <UserFriend, Long> {
    List<UserFriend> findAllByUser(User user);
    List<UserFriend> findAllByFriend(User friend);
    List<UserFriend> findAllByFriendAndStatus(User friend, Status status);
    List<UserFriend> findAllByUserAndStatus(User user, Status status);
    UserFriend findByUserAndFriend(User user, User friend);

    UserFriend findByUserAndFriendAndStatus(User user, User user1, Status status);
    UserFriend findByFriendAndUserAndStatus(User user, User user1, Status status);
}
