package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import com.example.justfriends.Services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserController {

    public UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    public UserFriendRepo userFriendRepo;
    public PostRepo postRepo;
    public CommentRepo commentRepo;
    public PictureRepo pictureRepo;
    public GalleryRepo galleryRepo;
    private final EmailService emailService;

    public UserController(UserRepo userRepo, PasswordEncoder passwordEncoder,
                          UserFriendRepo userFriendRepo, PostRepo postRepo, CommentRepo commentRepo,
                          PictureRepo pictureRepo, GalleryRepo galleryRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userFriendRepo = userFriendRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.galleryRepo = galleryRepo;
        this.pictureRepo = pictureRepo;
        this.emailService = emailService;
    }

    //Create User Account
    @GetMapping("/sign-up")
    public String showSignUp(Model model) {
        model.addAttribute("user", new User());

        return "user/register";
    }
    @PostMapping("/sign-up")
    public String registerUser(Model model,
                               @Valid User user,
                               Errors validation) {
        if (userRepo.findByEmail(user.getEmail()) != null){
            validation.rejectValue(
                    "email",
                    "user.email",
                    "This email is already in use."
            );
        }
        if (userRepo.findByUsername(user.getUsername()) != null){
            validation.rejectValue(
                    "username",
                    "user.username",
                    "This username is taken."
            );
        }
        if (validation.hasErrors()){
            model.addAttribute("user", user);
            model.addAttribute("errors", validation);

            return "user/register";
        }
        String hash = passwordEncoder.encode(user.getPassword());
        Date date = new Date();
        Gallery defaultGallery = new Gallery();
        defaultGallery.setName("Photos");
        defaultGallery.setUser(user);
        defaultGallery.setCreatedDate(date);
        Picture defaultPic = new Picture();
        defaultPic.setUser(user);
        defaultPic.setComment("Profile");
        defaultPic.setPictureUrl("/img/blank-profile-picture.png");
        defaultPic.setGallery(defaultGallery);
        user.setPassword(hash);
        user.setCreatedDate(date);
        user.setProfile_picture_url("/img/blank-profile-picture.png");
        User dbUser = userRepo.save(user);
        model.addAttribute("user", dbUser);

        emailService.prepareAndSend(user,"Welcome to JustFriends " + user.getUsername() + "!",
                "We're glad to have you! You might notice that your newsfeed is a little quiet. We want to avoid the" +
                        "drama that social media sites typically have of suggesting who you should be friends with or advertising" +
                        "to you. You probably want to make that decision for yourself! Instead, why not invite invite your friends" +
                        " to join, or send them a friend request if they " +
                        "already have!");
        return "redirect:/";
    }

    //Update User information
    @GetMapping("/edit/{username}")
    public String showEditProfile(Model model, @PathVariable String username) {
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        return "user/edit";
    }
    @PostMapping("/edit/{username}")
    public String editProfile(@PathVariable String username, @ModelAttribute User userToBeUpdated) {
        User user = userRepo.findByUsername(username);
        userToBeUpdated.setId(user.getId());
        userToBeUpdated.setUsername(user.getUsername());
        userToBeUpdated.setEmail(user.getEmail());
        userToBeUpdated.setPassword(user.getPassword());
        userToBeUpdated.setCreatedDate(user.getCreatedDate());
        userRepo.save(userToBeUpdated);

        return "redirect:/user/" + user.getUsername();
    }

    //Show User Profile page
    @GetMapping("/user/{username}")
    public String showUser(Model model,
                           @PathVariable String username) {
        User user = userRepo.findByUsername(username);
        List<UserFriend> userFriends = userFriendRepo.findAllByUserAndStatus(user, Status.ACCEPTED);// lists friends that you've accepted
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ArrayList<User> displayUsers = new ArrayList<>();// lists User objects of all the user's friends
        for (UserFriend userFriend : userFriends) {
            displayUsers.add(userFriend.getFriend());
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

        model.addAttribute("galleries", galleryRepo.findAllByUser(user));
        model.addAttribute("all-galleries", galleryRepo.findAll());
        model.addAttribute("friendsList", userFriends);
        model.addAttribute("user", user);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("friends",userFriendRepo.findAllByUserAndStatus(user,Status.ACCEPTED));

        model.addAttribute("comments", displayComments);
        model.addAttribute("posts", displayPosts);
        return "user/profile-page";
    }

    //Delete User account
    @PostMapping("/{username}/delete")
    public String deleteAccount(@PathVariable String username){
        User user = userRepo.findByUsername(username);
        List<Post> posts = postRepo.findAllByUserUsername(username);
        for (Post post : posts){
            postRepo.delete(post);
        }
        List<UserFriend> userFriends = userFriendRepo.findAllByUser(user);
        for (UserFriend userFriend : userFriends){
            userFriendRepo.delete(userFriend);
        }
        userRepo.delete(user);

        return "redirect:/login?logout";
    }

    //Show Home page
    @GetMapping("/")
    public String showTest(Model model, @ModelAttribute User user) {
        model.addAttribute("currentUser", user);

        return "index";
    }

    //layout prototype
    @GetMapping("/layout")
    public String showLayout(){
        return "partials/layout";
    }

}
