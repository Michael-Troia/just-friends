package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
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

    //Create Gallery
    @PostMapping("{username}/gallery/create")
    public String createGallery(@PathVariable String username,
                                @ModelAttribute Gallery gallery){
        User newUser = userRepo.findByUsername(username);
        Gallery newGallery = new Gallery();
        newGallery.setCreatedDate(new Date());
        newGallery.setName(gallery.getName());
        newGallery.setUser(newUser);
        galleryRepo.save(newGallery);

        return "redirect:/" + username + "/my-photos";
    }

    //View Gallery
    @GetMapping("{username}/gallery/{id}")
    public String showPhotosHome(@PathVariable String username,
                                 @PathVariable long id,
                                 Model model){
        User displayUser = userRepo.findByUsername(username);
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Gallery gallery = galleryRepo.findById(id);
        List<Picture> userPhotos = pictureRepo.findAllByGallery(gallery);

        model.addAttribute("displayUser", displayUser);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("gallery", gallery);
        model.addAttribute("photos", userPhotos);

        return "galleries/show";
    }

    //Update Gallery
    @PostMapping("{username}/gallery/{id}/edit")
    public String editGallery(@PathVariable String username,
                              @PathVariable long id,
                              @ModelAttribute Gallery galleryToBeUpdated){
        User user = userRepo.findByUsername(username);
        Gallery gallery = galleryRepo.findById(id);
        galleryToBeUpdated.setCreatedDate(gallery.getCreatedDate());
        galleryToBeUpdated.setUser(gallery.getUser());
        galleryRepo.save(galleryToBeUpdated);

        return "redirect:/" + username + "/gallery/{id}";
    }

    //Delete Gallery
    @PostMapping("/{username}/gallery/{id}/delete")
    public String deleteAccount(@PathVariable String username,
                                @PathVariable long id){
        User user = userRepo.findByUsername(username);
        List<Picture> pictures = galleryRepo.findById(id).getPictures();
        for (Picture picture : pictures){
            pictureRepo.delete(picture);
        }
        galleryRepo.delete(galleryRepo.findById(id));

        return "redirect:/" + username + "/my-photos";
    }
}
