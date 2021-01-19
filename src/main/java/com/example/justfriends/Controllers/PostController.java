package com.example.justfriends.Controllers;

import com.example.justfriends.Models.Comment;
import com.example.justfriends.Models.Post;
import com.example.justfriends.Models.User;
import com.example.justfriends.Models.UserFriend;
import com.example.justfriends.Repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
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

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
public class PostController {

    public UserRepo userRepo;
    public UserFriendRepo userFriendRepo;
    public PostRepo postRepo;
    public CommentRepo commentRepo;
    public PictureRepo pictureRepo;
    public GalleryRepo galleryRepo;

    public PostController(UserRepo userRepo, UserFriendRepo userFriendRepo, PostRepo postRepo, CommentRepo commentRepo,
                          PictureRepo pictureRepo, GalleryRepo galleryRepo) {
        this.userRepo = userRepo;
        this.userFriendRepo = userFriendRepo;
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.galleryRepo = galleryRepo;
        this.pictureRepo = pictureRepo;
    }


    @GetMapping("/posts/create/{username}")
    public String showCreatePostForm(Model model, @PathVariable String username) {
        model.addAttribute("user", userRepo.findByUsername(username));
        model.addAttribute("post", new Post());
        return "post/create";
    }

    @PostMapping("/posts/create/{username}")
    public String submitPostForm(@ModelAttribute Post post,
                                 @ModelAttribute User currentUser,
                                 @PathVariable String username,
                                 Model model) {
        Post newPost = new Post();
        User newUser = userRepo.findByUsername(username);
        newPost.setUser(newUser);
        newPost.setCreatedDate(new Date());
        newPost.setBody(post.getBody());
        Post dbPost = postRepo.save(newPost);

        model.addAttribute("user", newUser);
        model.addAttribute("post", dbPost);
        System.out.println(newUser.getUsername());
        return "redirect:/posts/view/" + newUser.getUsername();
    }

    //view posts
    @GetMapping("/posts/view/{username}")
    public String showAllUserPosts(Model model,
                                   @PathVariable String username) {
        List<Post> postList = postRepo.findAllByUserUsername(username);
        ArrayList<String> displayPosts = new ArrayList<>();
        int i = 1;
        for (Post post : postList) {
            String body = "Body of post #" + (i++) + ": " + post.getBody();
            displayPosts.add(body);
            String date = " , that post was made: " + post.getCreatedDate() + " . ";
            displayPosts.add(date);
            String id = "Post Id #:" + post.getId() + " .";
        }
        model.addAttribute("userPosts", displayPosts);
        model.addAttribute("postList" , postList);

        return "post/view";
    }

    //Edit Post
    @GetMapping("/posts/edit/{username}/{id}")
    public String viewEditPostForm(Model viewModel,
                                   @PathVariable long id,
                                   @PathVariable String username) {
        Post post = postRepo.getOne(id);
        User user = userRepo.findByUsername(username);

        viewModel.addAttribute("post", post);
        viewModel.addAttribute("user", user);

        return "post/edit";
    }

    @PostMapping("/posts/edit/{username}/{id}")
    public String editPost(
            @ModelAttribute Post postToBeUpdated,
            @PathVariable String username,
            @PathVariable long id,
            @ModelAttribute User currentUser) {
        Post post = postRepo.findById(id);
        User user = userRepo.findByUsername(username);
        Date editDate = new Date();

        postToBeUpdated.setEditDate(editDate);
        postToBeUpdated.setId(post.getId());
        postToBeUpdated.setComments(post.getComments());
        postToBeUpdated.setCreatedDate(post.getCreatedDate());
        postToBeUpdated.setUser(user);
        postToBeUpdated.setPhoto_url(post.getPhoto_url());
        System.out.println(postToBeUpdated.getId());
        Post dbPost = postRepo.save(postToBeUpdated);
        return "redirect:/posts/view/" + dbPost.getUser().getUsername();
    }

    @PostMapping("/posts/delete/{username}/{id}")
    public String deletePost(@PathVariable String username,
                             @PathVariable long id){
        Post post = postRepo.getOne(id);
        postRepo.delete(post);
        return "redirect:/posts/view/"+ username;
    }
}


    //test: post with ID of 1, linked to user with ID 1
//    Post post = postRepo.findById(1L);
//        model.addAttribute("post", post.getBody());
//    //test: comments from user with ID 2, linked to post_Id 1
//    Comment comment_userId_2 = commentRepo.findById(1L);
//        model.addAttribute("commentsFromUser2", comment_userId_2);
//    //test: comments from user with ID 3, linked to post_Id 1
//    Comment comment_userId_3 = commentRepo.findById(2L);
//        model.addAttribute("commentsFromUser3", comment_userId_3);

