package com.example.justfriends.Controllers;

import com.example.justfriends.Models.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepo extends JpaRepository <Gallery, Long> {

}
