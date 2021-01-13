package com.example.justfriends.Controllers;

import com.example.justfriends.Models.Comment;
import com.example.justfriends.Models.Post;
import com.example.justfriends.Models.User;
import com.example.justfriends.Models.UserFriend;
import com.example.justfriends.Repositories.CommentRepo;
import com.example.justfriends.Repositories.PostRepo;
import com.example.justfriends.Repositories.UserFriendRepo;
import com.example.justfriends.Repositories.UserRepo;
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
    private final PasswordEncoder passwordEncoder;
    public UserFriendRepo userFriendRepo;
    public PostRepo postRepo;
    public CommentRepo commentRepo;

    public UserController(UserRepo userRepo, PasswordEncoder passwordEncoder,
                          UserFriendRepo userFriendRepo, PostRepo postRepo, CommentRepo commentRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userFriendRepo = userFriendRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    @GetMapping("/sign-up")
    public String showSignUp(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute User user,
                               Model viewModel){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        User dbUser = userRepo.save(user);
        viewModel.addAttribute("user", dbUser);
//        return "register";
        return  "redirect:/show/"+dbUser.getId();
    }

    @GetMapping("/show/{id}")
    public String showUser(Model model,
                           @PathVariable long id){
        List<UserFriend> friendList = userFriendRepo.findAllByUserId(id);
        ArrayList<String> displayFriends = new ArrayList<>();

        for (UserFriend friend: friendList){
            String name = friend.getFriend().getUsername();
            displayFriends.add(name);
        }


        //test: display friends usernames of user with Id 1
        model.addAttribute("friendsList", displayFriends);
        //test: post with ID of 1, linked to user with ID 1
        Post post = postRepo.findById(1L);
        model.addAttribute("post", post.getBody());
        //test: comments from user with ID 2, linked to post_Id 1
        Comment comment_userId_2 = commentRepo.findById(1L);
        model.addAttribute("commentsFromUser2", comment_userId_2);
        //test: comments from user with ID 3, linked to post_Id 1
        Comment comment_userId_3 = commentRepo.findById(2L);
        model.addAttribute("commentsFromUser3", comment_userId_3);

        model.addAttribute(userRepo.getOne(id));
        return "show";
    }
}
