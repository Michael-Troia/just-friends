package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import com.example.justfriends.Services.EmailService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
        Gallery newGallery = new Gallery();
        newGallery.setName("Photos");
        newGallery.setUser(user);
        newGallery.setCreatedDate(new Date());

        Picture defaultPic = new Picture();
        defaultPic.setUser(user);
        defaultPic.setComment("Profile");
        defaultPic.setPictureUrl("/img/blank-profile-picture.png");
        defaultPic.setGallery(newGallery);
        user.setPassword(hash);
        user.setCreatedDate(new Date());
        user.setProfile_picture_url("/img/blank-profile-picture.png");
        User dbUser = userRepo.save(user);
        galleryRepo.save(newGallery);
        pictureRepo.save(defaultPic);
        model.addAttribute("user", dbUser);

//        emailService.prepareAndSend(user,"Welcome to JustFriends " + user.getUsername() + "!",
//                "We're glad to have you! You might notice that your newsfeed is a little quiet. We want to avoid the" +
//                        "drama that social media sites typically have of suggesting who you should be friends with or advertising" +
//                        "to you. You probably want to make that decision for yourself! Instead, why not invite invite your friends" +
//                        " to join, or send them a friend request if they " +
//                        "already have!");

        return "redirect:/login";
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
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

        List<UserFriend> userFriendRequests = userFriendRepo.findAllByFriendAndStatus(user, Status.PENDING);
        model.addAttribute("friendRequests", userFriendRequests);
        model.addAttribute("galleries", galleryRepo.findAllByUser(user));
        model.addAttribute("all-galleries", galleryRepo.findAll());
        model.addAttribute("user", user);
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("friends",myFriends);
        model.addAttribute("comments", displayComments);
        model.addAttribute("posts", displayPosts);
        model.addAttribute("newPost", new Post());
        model.addAttribute("newComment", new Comment());
        model.addAttribute("newGallery", new Gallery());
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
        List<Comment> comments = commentRepo.findAllByUser(user);
        for (Comment comment : comments) {
            commentRepo.delete(comment);
        }
        List<Picture> pictures = pictureRepo.findAllByUser(user);
        for (Picture picture : pictures) {
            pictureRepo.delete(picture);
        }
        List<Gallery> galleries = galleryRepo.findAllByUser(user);
        for (Gallery gallery : galleries) {
            galleryRepo.delete(gallery);
        }
        List<UserFriend> userFriends1 = userFriendRepo.findAllByUser(user);
        List<UserFriend> userFriends2 = userFriendRepo.findAllByFriend(user);
        for (UserFriend userFriend : userFriends1){
            userFriendRepo.delete(userFriend);
        }
        for (UserFriend userFriend : userFriends2) {
            userFriendRepo.delete(userFriend);
        }

        userRepo.delete(user);

        return "redirect:/login?logout";
    }

//    //Show Home page
//    @GetMapping("/")
//    public String showTest(Model model, @ModelAttribute User user) {
//        model.addAttribute("currentUser", user);
//        userRepo.findByUsername(user.getUsername());
//        return "index";
//    }

    @RequestMapping("/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken))
            return "redirect:/user/" + auth.getName();

        return "index";
    }
}

