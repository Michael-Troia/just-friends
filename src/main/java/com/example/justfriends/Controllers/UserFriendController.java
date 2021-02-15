package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import com.example.justfriends.Services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class UserFriendController {

    public UserRepo userRepo;
    public UserFriendRepo userFriendRepo;
    public PostRepo postRepo;
    public CommentRepo commentRepo;
    public PictureRepo pictureRepo;
    public GalleryRepo galleryRepo;
    private final EmailService emailService;

    public UserFriendController(UserRepo userRepo, UserFriendRepo userFriendRepo,
                                PostRepo postRepo, CommentRepo commentRepo,
                                PictureRepo pictureRepo, GalleryRepo galleryRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.userFriendRepo = userFriendRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.galleryRepo = galleryRepo;
        this.pictureRepo = pictureRepo;
        this.emailService = emailService;
    }

    //Create UserFriend (Friend request)
    @PostMapping("/request/{username}/{friendName}")
    public String addFriend(@PathVariable String username,
                            @PathVariable String friendName) {
        if (userFriendRepo.findByUserAndFriend(userRepo.findByUsername(username), userRepo.findByUsername(friendName)) != null) {
            return "redirect:/";
        }
        UserFriend userFriend = new UserFriend();
        userFriend.setFriend(userRepo.findByUsername(friendName));
        userFriend.setUser(userRepo.findByUsername(username));
        userFriend.setStatus(Status.PENDING);
        userFriendRepo.save(userFriend);

//        emailService.prepareAndSend(userRepo.findByUsername(friendName), friendName + ", someone wants to be your friend :)",
//                "Looks like you're popular! You might have a friend in " + username + " . Head to the friend request page on your" +
//                        " justfriends.online profile to let them know if you'd like to be friends!");

        return "redirect:/";
    }

    //Delete UserFriend (Unfriend)
    @PostMapping("/{username}/{friendName}/delete")
    public String deleteFriend(@PathVariable String username,
                               @PathVariable String friendName) {
        User user = userRepo.findByUsername(username);
        User friend = userRepo.findByUsername(friendName);
        UserFriend userUserFriend = userFriendRepo.findByUserAndFriend(user, friend);
        UserFriend friendUserFriend = userFriendRepo.findByUserAndFriend(friend, user);

        userFriendRepo.delete(userUserFriend);
        userFriendRepo.delete(friendUserFriend);

        return "redirect:/user/" + username;
    }

    //Read Friend requests
    @GetMapping("/{username}/friends/requests")
    public String showFriendRequests(@PathVariable String username,
                                     Model model) {
        User user = userRepo.findByUsername(username);
        List<UserFriend> userFriendRequests = userFriendRepo.findAllByFriendAndStatus(user, Status.PENDING);//requests someone else sent

        model.addAttribute("friendRequests", userFriendRequests);
        model.addAttribute("user", user);

        return "userFriend/friend-requests";
    }

    //Reject Friend request
    @PostMapping("/request/{username}/{friendName}/reject")
    public String rejectFriend(@PathVariable String username,
                               @PathVariable String friendName) {
        User user = userRepo.findByUsername(username);
        User friend = userRepo.findByUsername(friendName);

        List<UserFriend> userFriends = userFriendRepo.findAllByFriendAndStatus(user, Status.PENDING);
        for (UserFriend userFriend : userFriends) {
            userFriend.setStatus(Status.REJECTED);
            userFriendRepo.save(userFriend);
        }

//        UserFriend userUserFriend = userFriendRepo.findByUserAndFriend(user, friend);
//        userUserFriend.setStatus(Status.REJECTED);
//        userFriendRepo.save(userUserFriend);
//
//        UserFriend friendUserFriend = userFriendRepo.findByUserAndFriend(friend, user);
//        friendUserFriend.setStatus(Status.REJECTED);
//
//        userFriendRepo.save(userUserFriend);
//        userFriendRepo.save(friendUserFriend);

        return "redirect:/user/" + username;
    }

    //Accept Friend request
    @PostMapping("/request/{username}/{friendName}/accept")
    public String acceptFriend(@PathVariable String username,
                               @PathVariable String friendName) {
        User user = userRepo.findByUsername(username);
        User friend = userRepo.findByUsername(friendName);

        List<UserFriend> userFriends = userFriendRepo.findAllByFriendAndStatus(user, Status.PENDING);
        for (UserFriend userFriend : userFriends) {
            userFriend.setStatus(Status.ACCEPTED);
            userFriendRepo.save(userFriend);
        }

        return "redirect:/user/" + username;
    }

    //View Friend profile
    @GetMapping("/{username}/friend/{friendName}")
    public String showFriendProfile(@PathVariable String username,
                                    @PathVariable String friendName,
                                    Model model) {
        User currentUser = userRepo.findByUsername(username);
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User friend = userRepo.findByUsername(friendName);

        List<UserFriend> friendUserFriends1 = userFriendRepo.findAllByUserAndStatus(friend, Status.ACCEPTED);
        List<UserFriend> friendUserFriends2 = userFriendRepo.findAllByFriendAndStatus(friend, Status.ACCEPTED);
        ArrayList<User> friendFriends = new ArrayList<>();// lists User objects of friend's userFriends
        for (UserFriend userFriend : friendUserFriends1) {
            friendFriends.add(userFriend.getFriend());
        }
        for (UserFriend userFriend : friendUserFriends2) {
            friendFriends.add(userFriend.getUser());
        }

        List<UserFriend> userUserFriends1 = userFriendRepo.findAllByUserAndStatus(currentUser, Status.ACCEPTED);// user's friend list
        List<UserFriend> userUserFriends2 = userFriendRepo.findAllByFriendAndStatus(currentUser, Status.ACCEPTED);
        ArrayList<User> userFriends = new ArrayList<>();
        for (UserFriend userFriend : userUserFriends1) {
            userFriends.add(userFriend.getFriend());
        }
        for (UserFriend userFriend : userUserFriends2) {
            userFriends.add(userFriend.getUser());
        }

        List<Gallery> friendGalleries = galleryRepo.findAllByUser(friend);

        model.addAttribute("friendFriends", friendFriends);
        model.addAttribute("friend", friend);
        model.addAttribute("userFriendList", userFriends);
        model.addAttribute("currentUser", currentUser);
        if (!friendGalleries.isEmpty()) {
            model.addAttribute("galleries", friendGalleries);
        }
        model.addAttribute("sessionUser", sessionUser);

        return "userFriend/friend-profile";
    }

    //Discover friends page
    @GetMapping("/users/search/{username}")
    public String viewAllAdsWithAjax(@PathVariable String username,
                                     Model model) {
        User currentUser = userRepo.findByUsername(username);
        List<UserFriend> userUserFriends1 = userFriendRepo.findAllByUserAndStatus(currentUser, Status.ACCEPTED);// user's friend list
        List<UserFriend> userUserFriends2 = userFriendRepo.findAllByFriendAndStatus(currentUser, Status.ACCEPTED);
        ArrayList<User> userFriends = new ArrayList<>();
        for (UserFriend userFriend : userUserFriends1) {
            userFriends.add(userFriend.getFriend());
        }
        for (UserFriend userFriend : userUserFriends2) {
            userFriends.add(userFriend.getUser());
        }
        model.addAttribute("allUsers", userRepo.findAll());

        model.addAttribute("user", currentUser);
        model.addAttribute("userFriends", userFriends);
        return "user/search";
    }

    @PostMapping("/users/search/{username}")
    public String joinCohort(@RequestParam(name = "search") String search,
                             Model model,
                             @PathVariable String username) {
        User currentUser = userRepo.findByUsername(username);
        List<UserFriend> userUserFriends1 = userFriendRepo.findAllByUserAndStatus(currentUser, Status.ACCEPTED);// user's friend list
        List<UserFriend> userUserFriends2 = userFriendRepo.findAllByFriendAndStatus(currentUser, Status.ACCEPTED);
        List<User> userFriends = new ArrayList<>();
        for (UserFriend userFriend : userUserFriends1) {
            userFriends.add(userFriend.getFriend());
        }
        for (UserFriend userFriend : userUserFriends2) {
            userFriends.add(userFriend.getUser());
        }

        List<User> filteredList = new ArrayList<>();
        for (User user : userRepo.findAll()) {
            if (user.getUsername().toLowerCase().contains(search.toLowerCase())
                    || user.getFirstName().toLowerCase().contains(search.toLowerCase())
                    || user.getLastName().toLowerCase().contains(search.toLowerCase())) {
                filteredList.add(user);
            }
        }

        model.addAttribute("allUsers", filteredList);
        model.addAttribute("user", currentUser);
        model.addAttribute("userFriends", userFriends);
        model.addAttribute("search", search);
        return "user/search";
    }
}


//    @GetMapping("/users/search/{username}")
//    public String viewAllAdsWithAjax(@PathVariable String username,
//                                     Model model) {
//        User currentUser = userRepo.findByUsername(username);
//        List<UserFriend> userUserFriends1 = userFriendRepo.findAllByUserAndStatus(currentUser, Status.ACCEPTED);// user's friend list
//        List<UserFriend> userUserFriends2 = userFriendRepo.findAllByFriendAndStatus(currentUser, Status.ACCEPTED);
//        ArrayList<User> userFriends = new ArrayList<>();
//        for (UserFriend userFriend : userUserFriends1) {
//            userFriends.add(userFriend.getFriend());
//        }
//        for (UserFriend userFriend : userUserFriends2) {
//            userFriends.add(userFriend.getUser());
//        }
//        model.addAttribute("allUsers", userRepo.findAll());
//
//        model.addAttribute("currentUser", currentUser);
//        model.addAttribute("userFriends", userFriends);
//        return "user/search";
//    }
//}