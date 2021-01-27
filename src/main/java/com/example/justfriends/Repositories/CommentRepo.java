package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Comment;
import com.example.justfriends.Models.Post;
import com.example.justfriends.Models.User;
import com.example.justfriends.Models.UserFriend;
import javassist.util.proxy.ProxyObjectOutputStream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository <Comment, Long> {

    Comment findByBody(String body);
    List<Comment> findAllByUserId(long id);
    Comment findById(long id);
    List<Comment> findAllByParentPost(Post parentPost);
    List<Comment> findAllByUser(User user);
}
