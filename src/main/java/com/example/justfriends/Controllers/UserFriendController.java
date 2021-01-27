package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
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
    //List all users
    @GetMapping("/{username}/friends")
    public String showUsers(Model model,
                            @PathVariable String username) {
        List<User> userList = userRepo.findAllByIdGreaterThan(0);
        model.addAttribute("userList", userList);

        return "userFriend/create";
    }
    //start a new friend request
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

    //view friends
    @GetMapping("/{username}/friends/view")
    public String showFriendList(@PathVariable String username,
                                 Model model){
        User user = userRepo.findByUsername(username);
        List<UserFriend> userFriends = userFriendRepo.findAllByUser(user);//might need to include other people's requests
        model.addAttribute("friends" , userFriends);

        return "userFriend/view";
    }

    //delete friendship
    @PostMapping("/{username}/{friendId}/delete")
    public String deleteFriend(@PathVariable String username,
                               @PathVariable long friendId){
        UserFriend userFriend = userFriendRepo.findById(friendId);
        userFriendRepo.delete(userFriend);

        return "redirect:/"+username+"/friends/view";
    }

    //view friend requests
    @PostMapping("/{username}/friends/requests")
    public String showFriendRequests(@PathVariable String username,
                                     Model model){
        User user = userRepo.findByUsername(username);
        //returns a list of userFriends with the current user as the friend
        List<UserFriend> userFriendRequests = userFriendRepo.findAllByFriendAndStatus(user, Status.PENDING);
        model.addAttribute("friendRequests", userFriendRequests);

        return "userFriend/friend-requests";
    }

    //reject friendship
    @PostMapping("/{username}/{friendId}/reject")
    public String rejectFriend(@PathVariable String username,
                               @PathVariable long friendId){
        UserFriend userFriend = userFriendRepo.findById(friendId);
        userFriend.setStatus(Status.REJECTED);
        userFriendRepo.save(userFriend);

        return "redirect:/"+username+"/friends/view";
    }
    //accept friendship
    @PostMapping("/{username}/{friendId}/accept")
    public String acceptFriend(@PathVariable String username,
                               @PathVariable long friendId){
        UserFriend userFriendRequester = userFriendRepo.findById(friendId);
        userFriendRequester.setStatus(Status.ACCEPTED);
        userFriendRepo.save(userFriendRequester);

        UserFriend newUserFriend = new UserFriend();
        newUserFriend.setFriend(userFriendRequester.getUser());
        newUserFriend.setStatus(Status.ACCEPTED);
        newUserFriend.setUser(userRepo.findByUsername(username));
        UserFriend db_NewUserFriend = userFriendRepo.save(newUserFriend);
        return "redirect:/"+username+"/friends/view";
    }

    @GetMapping("/{username}/stories")
    public String showNewsFeed(@PathVariable String username,
                                     Model model){
        User currentUser = userRepo.findByUsername(username);

        List<UserFriend> userFriends = userFriendRepo.findAllByUserAndStatus(currentUser, Status.ACCEPTED);// lists friends that you've accepted
        ArrayList<User> displayUsers = new ArrayList<>();// lists User objects of all the users friends
        for (UserFriend userFriend : userFriends) {
            displayUsers.add(userFriend.getFriend());
        }
        displayUsers.add(currentUser);// includes your own posts in stories view


        ArrayList<Post> displayPosts = new ArrayList<>();// lists all posts by all friends and the user
        ArrayList<Comment> displayComments = new ArrayList<>();// lists all comments to all posts by all friends and user
        for (User displayUser : displayUsers) {
            for (Post post : postRepo.findAllByUser(displayUser)) {
                displayPosts.add(post);
                displayComments.addAll(commentRepo.findAllByParentPost(post));
            }
        }


        model.addAttribute("comments", displayComments);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", displayPosts);

        return "userFriend/stories";
    }
}