package com.example.justfriends.Controllers;

import com.example.justfriends.Models.Gallery;
import com.example.justfriends.Models.Picture;
import com.example.justfriends.Models.User;
import com.example.justfriends.Repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PhotoController {

    public UserRepo userRepo;
    public UserFriendRepo userFriendRepo;
    public PostRepo postRepo;
    public CommentRepo commentRepo;
    public PictureRepo pictureRepo;
    public GalleryRepo galleryRepo;

    public PhotoController(UserRepo userRepo, UserFriendRepo userFriendRepo, PostRepo postRepo, CommentRepo commentRepo,
                          PictureRepo pictureRepo, GalleryRepo galleryRepo) {
        this.userRepo = userRepo;
        this.userFriendRepo = userFriendRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.galleryRepo = galleryRepo;
        this.pictureRepo = pictureRepo;
    }

    @GetMapping("{username}/my-photos")
    public String showPhotosHome(@PathVariable String username,
                                 Model model){
        User currentUser = userRepo.findByUsername(username);
        List<Picture> userPhotos = pictureRepo.findAllByUser(currentUser);
        List<Gallery> userGalleries = galleryRepo.findAllByUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("photos", userPhotos);
        model.addAttribute("galleries", userGalleries);

        return "galleries/my-photos";
    }

    @GetMapping("{username}/gallery/{id}")
    public String showPhotosHome(@PathVariable String username,
                                 @PathVariable long id,
                                 Model model){
        User currentUser = userRepo.findByUsername(username);
        Gallery gallery = galleryRepo.findById(id);
        List<Picture> userPhotos = pictureRepo.findAllByGallery(gallery);

        model.addAttribute("user", currentUser);
        model.addAttribute("gallery", gallery);
        model.addAttribute("photos", userPhotos);

        return "galleries/show";
    }
}
