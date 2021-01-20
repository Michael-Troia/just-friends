package com.example.justfriends.Controllers;

import com.example.justfriends.Models.Post;
import com.example.justfriends.Models.Status;
import com.example.justfriends.Models.User;
import com.example.justfriends.Models.UserFriend;
import com.example.justfriends.Repositories.*;
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
public class UserFriendController {

    public UserRepo userRepo;
    public UserFriendRepo userFriendRepo;
    public PostRepo postRepo;
    public CommentRepo commentRepo;
    public PictureRepo pictureRepo;
    public GalleryRepo galleryRepo;

    public UserFriendController(UserRepo userRepo, UserFriendRepo userFriendRepo,
                                PostRepo postRepo, CommentRepo commentRepo,
                          PictureRepo pictureRepo, GalleryRepo galleryRepo) {
        this.userRepo = userRepo;
        this.userFriendRepo = userFriendRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.galleryRepo = galleryRepo;
        this.pictureRepo = pictureRepo;
    }
    //create UserFriend
    @GetMapping("/{username}/friends")
    public String showUsers(Model model,
                            @PathVariable String username) {
        List<User> userList = userRepo.findAllByIdGreaterThan(0);
        model.addAttribute("userList", userList);

        return "userFriend/create";
    }

    @PostMapping("/{username}/{friendName}")
    public String addFriend(@PathVariable String username,
                            @PathVariable String friendName){
        UserFriend userFriend = new UserFriend();
        userFriend.setFriend(userRepo.findByUsername(friendName));
        userFriend.setStatus(Status.PENDING);
        userFriend.setUser(userRepo.findByUsername(username));
        UserFriend db_userFriend = userFriendRepo.save(userFriend);

        return "redirect:/";
    }

    @GetMapping("/{username}/friends/view")
    public String showFriendList(@PathVariable String username,
                                 Model model){
        User user = userRepo.findByUsername(username);
        List<UserFriend> userFriends = userFriendRepo.findAllByUser(user);
        model.addAttribute("friends" , userFriends);

        return "userFriend/view";
    }

    @PostMapping("/{username}/{friendId}/delete")
    public String deleteFriend(@PathVariable String username,
                               @PathVariable long friendId){
        UserFriend userFriend = userFriendRepo.findById(friendId);
        userFriendRepo.delete(userFriend);

        return "redirect:/"+username+"/friends/view";
    }


}