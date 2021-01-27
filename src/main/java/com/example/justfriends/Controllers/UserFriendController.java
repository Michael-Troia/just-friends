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

    //Create UserFriend (Friend request)
    @PostMapping("/{username}/{friendName}")
    public String addFriend(@PathVariable String username,
                            @PathVariable String friendName){
        UserFriend userFriend = new UserFriend();
        userFriend.setFriend(userRepo.findByUsername(friendName));
        userFriend.setStatus(Status.PENDING);
        userFriend.setUser(userRepo.findByUsername(username));
        userFriendRepo.save(userFriend);

        return "redirect:/";
    }

    //View Friendslist
    @GetMapping("/{username}/friends/view")
    public String showFriendList(@PathVariable String username,
                                 Model model){
        User user = userRepo.findByUsername(username);
        List<UserFriend> userFriends = userFriendRepo.findAllByUser(user);
        model.addAttribute("friends" , userFriends);

        return "userFriend/view";
    }

    //Delete UserFriend (Unfriend)
    @PostMapping("/{username}/{friendId}/delete")
    public String deleteFriend(@PathVariable String username,
                               @PathVariable long friendId){
        UserFriend userFriend = userFriendRepo.findById(friendId);
        userFriendRepo.delete(userFriend);

        return "redirect:/"+username+"/friends/view";
    }

    //Read Friend requests
    @PostMapping("/{username}/friends/requests")
    public String showFriendRequests(@PathVariable String username,
                                     Model model){
        User user = userRepo.findByUsername(username);
        List<UserFriend> userFriendRequests = userFriendRepo.findAllByFriendAndStatus(user, Status.PENDING);
        model.addAttribute("friendRequests", userFriendRequests);

        return "userFriend/friend-requests";
    }

    //Reject Friend request
    @PostMapping("/{username}/{friendId}/reject")
    public String rejectFriend(@PathVariable String username,
                               @PathVariable long friendId){
        UserFriend userFriend = userFriendRepo.findById(friendId);
        userFriend.setStatus(Status.REJECTED);
        userFriendRepo.save(userFriend);

        return "redirect:/"+username+"/friends/view";
    }

    //Accept Friend request
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
        userFriendRepo.save(newUserFriend);

        return "redirect:/"+username+"/friends/view";
    }

    @GetMapping("/{username}/stories")
    public String showNewsFeed(@PathVariable String username,
                                     Model model){
        User currentUser = userRepo.findByUsername(username);
        List<UserFriend> userFriends = userFriendRepo.findAllByUserAndStatus(currentUser, Status.ACCEPTED);// lists friends that you've accepted
        ArrayList<User> displayUsers = new ArrayList<>();// lists User objects of all the user's friends
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

    @GetMapping("/{username}/friend/{friendName}")
    public String showFriendProfile(@PathVariable String username,
                                    @PathVariable String friendName,
                                    Model model){
        User currentUser = userRepo.findByUsername(username);
        User friend = userRepo.findByUsername(friendName);

        List<UserFriend> friendUserFriends = userFriendRepo.findAllByUserAndStatus(friend, Status.ACCEPTED);// friend's friend list
        ArrayList<User> friendFriends = new ArrayList<>();// lists User objects of friend's userfriends
        for (UserFriend friendUserFriend : friendUserFriends) {
            friendFriends.add(friendUserFriend.getFriend());
        }

        List<UserFriend> userFriends = userFriendRepo.findAllByUserAndStatus(currentUser, Status.ACCEPTED);// user's friend list

        List<UserFriend> mutualUserFriends = userFriendRepo.findAllByUserAndFriendAndStatus(currentUser, friend, Status.ACCEPTED);
        ArrayList<User> mutualFriends = new ArrayList<>();// lists User objects of all the mutual friends
        for (UserFriend mutualFriend : mutualUserFriends) {
            mutualFriends.add(mutualFriend.getUser());
        }


        model.addAttribute("friendFriends" ,friendFriends);
        model.addAttribute("friend", friend);
//        model.addAttribute("userFriendList" , userFriends);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("mutualFriends", mutualFriends);
        return "userFriend/friend-profile";
    }
}