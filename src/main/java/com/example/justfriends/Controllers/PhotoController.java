package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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

    //Show my-photos
    @GetMapping("{username}/my-photos")
    public String showPhotosHome(@PathVariable String username,
                                 Model model){
        User currentUser = userRepo.findByUsername(username);
        List<Picture> userPhotos = pictureRepo.findAllByUser(currentUser);
        List<Gallery> userGalleries = galleryRepo.findAllByUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("photos", userPhotos);
        model.addAttribute("galleries", userGalleries);
        model.addAttribute("gallery", new Gallery());

        return "galleries/my-photos";
    }

    //Create Gallery
    @PostMapping("{username}/gallery/create")
    public String createGallery(@PathVariable String username,
                                @Valid Gallery gallery,
                                Errors validation,
                                Model model){
        if (validation.hasErrors()){
            User currentUser = userRepo.findByUsername(username);
            List<Picture> userPhotos = pictureRepo.findAllByUser(currentUser);
            List<Gallery> userGalleries = galleryRepo.findAllByUser(currentUser);
            model.addAttribute("user", currentUser);
            model.addAttribute("photos", userPhotos);
            model.addAttribute("galleries", userGalleries);
            model.addAttribute("gallery", gallery);
            model.addAttribute("errors", validation);
            return "galleries/my-photos";
        }
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
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findByUsername(username);

        Gallery gallery = galleryRepo.findById(id);
        User displayGalleryUser = gallery.getUser();

        List<Picture> userPhotos = pictureRepo.findAllByGallery(gallery);

        List<UserFriend> userFriends1 = userFriendRepo.findAllByUserAndStatus(user, Status.ACCEPTED);// lists friends who accepted you
        List<UserFriend> userFriends2 = userFriendRepo.findAllByFriendAndStatus(user, Status.ACCEPTED);// lists friends who you accepted

        //collects all accepted friendships involving the user
        ArrayList<User> displayUsers = new ArrayList<>();
        ArrayList<User> myFriends = new ArrayList<>();
        for (UserFriend userFriend : userFriends1) {
            displayUsers.add(userFriend.getFriend());
            myFriends.add(userFriend.getFriend());
        }
        for (UserFriend userFriend : userFriends2) {
            displayUsers.add(userFriend.getUser());
            myFriends.add(userFriend.getUser());
        }
        displayUsers.add(user);// includes your own posts in stories view

        ArrayList<Post> displayPosts = new ArrayList<>();// lists all posts by all friends and the user
        ArrayList<Comment> displayComments = new ArrayList<>();// lists all comments to all posts by all friends and user
        for (User displayUser : displayUsers) {
            for (Post post : postRepo.findAllByUser(displayUser)) {
                displayPosts.add(post);
                displayComments.addAll(commentRepo.findAllByParentPost(post));
            }
        }

        model.addAttribute("displayUser", displayGalleryUser);
        model.addAttribute("displayPhoto", new Picture());
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("gallery", gallery);
        model.addAttribute("photos", userPhotos);
        model.addAttribute("galleries", galleryRepo.findAllByUser(user));
        model.addAttribute("all-galleries", galleryRepo.findAll());
        model.addAttribute("friendsList", myFriends);
        model.addAttribute("user", user);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("friends",userFriendRepo.findAllByUserAndStatus(user,Status.ACCEPTED));
        model.addAttribute("comments", displayComments);
        model.addAttribute("posts", displayPosts);
        model.addAttribute("picture", new Picture());
        model.addAttribute("newPost", new Post());

        return "galleries/show";
    }

    //Update Gallery
    @PostMapping("{username}/gallery/{id}/edit")
    public String editGallery(@PathVariable String username,
                              @PathVariable long id,
                              @Valid Gallery galleryToBeUpdated,
                              Errors validation,
                              Model model){
        User user = userRepo.findByUsername(username);
        Gallery gallery = galleryRepo.findById(id);
        galleryToBeUpdated.setCreatedDate(gallery.getCreatedDate());
        galleryToBeUpdated.setUser(gallery.getUser());
        if (validation.hasErrors()){
            return "redirect:/" + username + "/gallery/" + id;
        }
        galleryRepo.save(galleryToBeUpdated);

        return "redirect:/" + username + "/gallery/" + id;
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
        model.addAttribute("user", user);
        model.addAttribute("gallery", gallery);
        model.addAttribute("picture", new Picture());

        return "galleries/create-photo";
    }
    @PostMapping("/{username}/gallery/{id}/create-photo")
    public String submitCreatePhotoForm(@PathVariable String username,
                                        @PathVariable long id,
                                        @Valid Picture picture,
                                        Errors validation,
                                        Model model){
        if (validation.hasErrors()){
            model.addAttribute("errors", validation);
            model.addAttribute("picture", picture);
            model.addAttribute("user", userRepo.findByUsername(username));
            model.addAttribute("gallery", galleryRepo.findById(id));
            return "galleries/create-photo";
        }
        Picture newPicture = new Picture();
        User user = userRepo.findByUsername(username);
        Gallery gallery = galleryRepo.findById(id);
        newPicture.setPictureUrl(picture.getPictureUrl());
        newPicture.setGallery(gallery);
        newPicture.setUser(user);
        newPicture.setComment(picture.getComment());
        pictureRepo.save(newPicture);

        return "redirect:/" + username + "/gallery/" + id;
    }

    //Update photo comment
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
