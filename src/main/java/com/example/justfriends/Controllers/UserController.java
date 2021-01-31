package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    public UserController(UserRepo userRepo, PasswordEncoder passwordEncoder,
                          UserFriendRepo userFriendRepo, PostRepo postRepo, CommentRepo commentRepo,
                          PictureRepo pictureRepo, GalleryRepo galleryRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userFriendRepo = userFriendRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.galleryRepo = galleryRepo;
        this.pictureRepo = pictureRepo;
    }

    //Create User Account
    @GetMapping("/sign-up")
    public String showSignUp(Model model) {
        model.addAttribute("user", new User());

        return "user/register";
    }
    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute User user,
                               Model viewModel) {
        String hash = passwordEncoder.encode(user.getPassword());
        Date date = new Date();
        user.setPassword(hash);
        user.setCreatedDate(date);
        User dbUser = userRepo.save(user);
        viewModel.addAttribute("user", dbUser);
        System.out.println(dbUser.getUsername());

        return "redirect:/" + dbUser.getUsername();
    }

    //Update User information
    @GetMapping("/edit/{username}")
    public String showEditProfile(Model viewModel, @PathVariable String username) {
        viewModel.addAttribute("user", userRepo.findByUsername(username));

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
        System.out.println(userToBeUpdated.getUsername());
        System.out.println(user.getUsername());
        System.out.println(userToBeUpdated.getJob());
        userRepo.save(userToBeUpdated);

        return "redirect:/" + userToBeUpdated.getUsername();
    }

    //Show User Profile page
    @GetMapping("/{username}")
    public String showUser(Model model,
                           @PathVariable String username) {
        User user = userRepo.findByUsername(username);
        List<UserFriend> userFriends = userFriendRepo.findAllByUserAndStatus(user, Status.ACCEPTED);// lists friends that you've accepted
        model.addAttribute("friendsList", userFriends);
        model.addAttribute("user", user);

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

        return "user/home";
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

}
