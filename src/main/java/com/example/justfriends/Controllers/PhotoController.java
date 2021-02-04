package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
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
        if (gallery.getName().isBlank()){
            newGallery.setName("Photos");
        } else {
            newGallery.setName(gallery.getName());
        }
        newGallery.setUser(newUser);
        galleryRepo.save(newGallery);

        Picture newPicture = new Picture();
        newPicture.setGallery(newGallery);
        newPicture.setPictureUrl("/img/thumbnailDefault.jpg");
        newPicture.setComment("default");
        newPicture.setUser(newUser);
        pictureRepo.save(newPicture);
        galleryRepo.save(newGallery);

        return "redirect:/" + username + "/gallery/" + newGallery.getId();
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
        model.addAttribute("displayPhoto", new Picture());
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

    //Create Photo
    @GetMapping("/{username}/gallery/{id}/create-photo")
    public String showCreatePhotoForm(@PathVariable String username,
                                      @PathVariable long id,
                                      Model model){
        User user = userRepo.findByUsername(username);
        Gallery gallery = galleryRepo.findById(id);
        Picture newPicture = new Picture();

        model.addAttribute("user", user);
        model.addAttribute("gallery", gallery);
        model.addAttribute("newPicture", newPicture);

        return "galleries/create-photo";
    }
    @PostMapping("/{username}/gallery/{id}/create-photo")
    public String submitCreatePhotoForm(@PathVariable String username,
                                        @PathVariable long id,
                                        @ModelAttribute Picture picture){
        Picture newPicture = new Picture();
        User user = userRepo.findByUsername(username);
        Gallery gallery = galleryRepo.findById(id);
//        if (picture.getPictureUrl().isBlank()){
//
//
//        }
        newPicture.setPictureUrl(picture.getPictureUrl());
        newPicture.setGallery(gallery);
        newPicture.setUser(user);
        newPicture.setComment(picture.getComment());
        pictureRepo.save(newPicture);

        return "redirect:/" + username + "/gallery/" + id;
    }

    //Update photo
    @PostMapping("{username}/photo/{id}/edit")
    public String editPhoto(@PathVariable String username,
                              @PathVariable long id,
                              @ModelAttribute Picture pictureToBeUpdated){
        User user = userRepo.findByUsername(username);
        Picture picture = pictureRepo.findById(id);
        picture.setUser(user);
        picture.setGallery(picture.getGallery());
        picture.setId(id);
        picture.setComment(pictureToBeUpdated.getComment());

        pictureRepo.save(picture);

        return "redirect:/" + username + "/gallery/" + picture.getGallery().getId();
    }

    //Delete Photo
    @PostMapping("/{username}/photo/{id}/delete")
    public String deletePhoto(@PathVariable String username,
                              @PathVariable long id){
        User user = userRepo.findByUsername(username);
        Picture picture = pictureRepo.findById(id);
        Gallery gallery = galleryRepo.findById(picture.getGallery().getId());

        pictureRepo.delete(picture);
//
//        if (galleryRepo.findAll() == null) {
//            Picture defaultPicture =  new Picture();
//            defaultPicture.setUser(user);
//            defaultPicture.setComment("Photo");
//            defaultPicture.setPictureUrl("img/thumbnailDefault.jpg");
//            defaultPicture.setGallery(gallery);
//            pictureRepo.save(defaultPicture);
//        }


        return "redirect:/" + username + "/gallery/" + gallery.getId();
    }


}
