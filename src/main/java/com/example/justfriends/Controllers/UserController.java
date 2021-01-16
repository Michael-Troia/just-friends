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

    @GetMapping("/sign-up")
    public String showSignUp(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute User user,
                               Model viewModel) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        User dbUser = userRepo.save(user);
        viewModel.addAttribute("user", dbUser);
        System.out.println(dbUser.getUsername());
        return "redirect:/" + dbUser.getUsername();
    }

//    todo THIS REQUEST IS BROKEN
//    Show User Posts
    @PostMapping("/posts/create/{username}")
    public String showPosts(){
        return "post/view";
    }


    //Test: this test requires users, comments, userfriends, and post entries in database
    @GetMapping("/{username}")
    public String showUser(Model model,
                           @PathVariable String username) {
        User user = userRepo.findByUsername(username);
        List<UserFriend> friendList = userFriendRepo.findAllByUserUsername(username);
        ArrayList<String> displayFriends = new ArrayList<>();
        int i = 1;
        for (UserFriend friend : friendList) {
            String name = "Friend " + (i++) + ": " + friend.getFriend().getUsername();
            displayFriends.add(name);
            String status = " , Status of that friendship: " + friend.getStatus() + " . ";
            displayFriends.add(status);
        }
        //test: display friends usernames of user with Id 1
        model.addAttribute("friendsList", displayFriends);

        model.addAttribute("user", user);
        return "user/profile-page";
    }



    @GetMapping("/")
    public String showTest(Model model, @ModelAttribute User user) {


        model.addAttribute("currentUser", user);

        return "user/home";
    }

}
