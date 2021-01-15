package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
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
        return "register";
    }

    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute User user,
                               Model viewModel) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        User dbUser = userRepo.save(user);
        viewModel.addAttribute("user", dbUser);
//        return "register";
        return "redirect:/show/" + dbUser.getId();
    }

    //Test: this test requires users, comments, userfriends, and post entries in database
    @GetMapping("/show/{id}")
    public String showUser(Model model,
                           @PathVariable long id) {
        List<UserFriend> friendList = userFriendRepo.findAllByUserId(id);
        ArrayList<String> displayFriends = new ArrayList<>();

        for (UserFriend friend : friendList) {
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

    @GetMapping("/")
    public String showTest(Model model) {
        //todo Test: this code creates test entires in the database to test user/userfriend relationship
        //note: after first build, database must be dropped or setters must be commented out
        User user1 = new User();
        user1.setPassword("1234");
        user1.setEmail("email1");
        user1.setFirstName("first1");
        user1.setLastName("last1");
        user1.setUsername("username1");
        userRepo.save(user1);

        User user2 = new User();
        user2.setPassword("1234");
        user2.setEmail("email2");
        user2.setFirstName("first2");
        user2.setLastName("last2");
        user2.setUsername("username2");
        userRepo.save(user2);

        User user3 = new User();
        user3.setPassword("1234");
        user3.setEmail("email3");
        user3.setFirstName("first3");
        user3.setLastName("last3");
        user3.setUsername("username3");
        userRepo.save(user3);

        UserFriend userFriend1 = new UserFriend();
        userFriend1.setUser(user1);
        userFriend1.setFriend(user2);
        userFriend1.setStatus(Status.ACCEPTED);
        userFriendRepo.save(userFriend1);

        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUser(user1);
        userFriend2.setFriend(user3);
        userFriend2.setStatus(Status.PENDING);
        userFriendRepo.save(userFriend2);


        List<UserFriend> friendList = userFriendRepo.findByUserUsername("username1");
        ArrayList<String> TestFriendsList = new ArrayList<>();

        for (UserFriend friend : friendList) {
            String name = (friend.getFriend().getUsername() +  " status: ");
            String status = (friend.getStatus().toString());
            TestFriendsList.add(name);
            TestFriendsList.add(status);
        }

        model.addAttribute("user1friends", TestFriendsList);

        return "friends_list";
    }

}
