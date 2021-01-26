package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Picture;
import com.example.justfriends.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepo extends JpaRepository <Picture, Long> {

    Picture findById(long id);
//    Picture findByPictureUrl(String url);
    List<Picture> findAllByUser(User user);
}
