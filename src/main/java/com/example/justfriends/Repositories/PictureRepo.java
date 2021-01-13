package com.example.justfriends.Repositories;

import com.example.justfriends.Models.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepo extends JpaRepository <Picture, Long> {

}
