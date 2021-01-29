package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Gallery;
import com.example.justfriends.Models.Picture;
import com.example.justfriends.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepo extends JpaRepository <Picture, Long> {

    Picture findById(long id);
    List<Picture> findAllByUser(User user);
    Picture findByPictureUrl(String pictureUrl);
    List<Picture> findAllByGallery(Gallery gallery);
}
