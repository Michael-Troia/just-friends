package com.example.justfriends.Controllers;

import com.example.justfriends.Models.Comment;
import com.example.justfriends.Models.Post;
import com.example.justfriends.Models.User;
import com.example.justfriends.Models.UserFriend;
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

import java.time.LocalDate;
import java.time.Month;
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

    //Create Post
    @PostMapping("/posts/create/{username}")
    public String submitPostForm(@ModelAttribute Post post,
                                 @PathVariable String username,
                                 Model model) {
        Post newPost = new Post();
        User user = userRepo.findByUsername(username);
        newPost.setUser(user);

        LocalDate currentdate = LocalDate.now();
        int currentDay = currentdate.getDayOfMonth();
        Month currentMonth = currentdate.getMonth();
        String monthString = currentMonth.toString();
        String formatMonth = Character.toUpperCase(monthString.charAt(0)) + monthString.substring(1).toLowerCase();
        int currentYear = currentdate.getYear();

        newPost.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
        newPost.setCreatedDate(new Date());
        newPost.setBody(post.getBody());
        if (!post.getPhoto_url().isEmpty()) {
            newPost.setPhoto_url(post.getPhoto_url());
        }
        Post dbPost = postRepo.save(newPost);
        model.addAttribute("user", user);
        model.addAttribute("post", dbPost);
        System.out.println(user.getUsername());

        return "redirect:/user/" + username;
    }

    //Update Post
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
    public String editPost(@ModelAttribute Post postToBeUpdated,
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
        postToBeUpdated.setDateString(post.getDateString());
        postToBeUpdated.setUser(user);
        postToBeUpdated.setPhoto_url(post.getPhoto_url());
        System.out.println(postToBeUpdated.getId());
        Post dbPost = postRepo.save(postToBeUpdated);

        return "redirect:/user/" + username;
    }

    //Delete Post
    @PostMapping("/posts/delete/{username}/{id}")
    public String deletePost(@PathVariable String username,
                             @PathVariable long id){
        Post post = postRepo.getOne(id);
        postRepo.delete(post);

        return "redirect:/user/" + username;
    }

    //--- Comments ---//
    //Create Comment
    @PostMapping("/posts/create/{username}/{postId}/comment")
    public String submitCommentForm(@ModelAttribute Comment comment,
                                 //@ModelAttribute Post post,
                                 @PathVariable long postId,
                                 @PathVariable String username,
                                 Model model) {
        Post parentPost = postRepo.findById(postId);
        Comment newComment = new Comment();
        User newUser = userRepo.findByUsername(username);
        newComment.setUser(newUser);
        newComment.setCreatedDate(new Date());
        LocalDate currentdate = LocalDate.now();
        int currentDay = currentdate.getDayOfMonth();
        Month currentMonth = currentdate.getMonth();
        String monthString = currentMonth.toString();
        String formatMonth = Character.toUpperCase(monthString.charAt(0)) + monthString.substring(1).toLowerCase();
        int currentYear = currentdate.getYear();

        newComment.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
        newComment.setBody(comment.getBody());
        newComment.setParentPost(parentPost);
        Comment dbComment = commentRepo.save(newComment);
        model.addAttribute("user", newUser);
        model.addAttribute("comment", dbComment);
        System.out.println(newUser.getUsername());

        return "redirect:/user/" + username ;
    }

    //Update Comment
    @GetMapping("/comments/edit/{username}/{commentId}")
    public String viewEditCommentForm(@PathVariable String username,
                                      @PathVariable long commentId,
                                      Model model){
        Comment comment = commentRepo.getOne(commentId);
        User user = userRepo.findByUsername(username);
        model.addAttribute("comment", comment);
        model.addAttribute("user", user);

        return "post/comment-edit";
    }
    @PostMapping("/comments/edit/{username}/{commentId}")
    public String editComment(@ModelAttribute Comment commentToBeUpdated,
            @PathVariable String username,
            @PathVariable long commentId,
            @ModelAttribute User currentUser) {
        Comment comment = commentRepo.findById(commentId);
        User user = userRepo.findByUsername(username);

        commentToBeUpdated.setEditDate(new Date());
        commentToBeUpdated.setParentPost(comment.getParentPost());
        commentToBeUpdated.setId(comment.getId());
        commentToBeUpdated.setCreatedDate(comment.getCreatedDate());
        commentToBeUpdated.setDateString(comment.getDateString());
        commentToBeUpdated.setBody(commentToBeUpdated.getBody());
        commentToBeUpdated.setUser(user);
        commentToBeUpdated.setPhoto_url(comment.getPhoto_url());
        commentRepo.save(commentToBeUpdated);

        return "redirect:/user/" + username;
    }

    //Delete Comment
    @PostMapping("/comments/delete/{username}/{commentId}")
    public String deleteComment(@PathVariable String username,
                                @PathVariable long commentId) {
        Comment comment = commentRepo.findById(commentId);
        commentRepo.delete(comment);

        return "redirect:/user/" + username;
    }
}
