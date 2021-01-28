package com.example.justfriends;
import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import org.hamcrest.core.StringContains;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JustfriendsApplication.class)
@AutoConfigureMockMvc
public class IntegrationTest {

    private User testUser1;
    private User testUser2;
    private Post testPost;
    private Post testPost2;
    private Comment testComment1;
    private Comment testComment2;
    private List<Comment> testComments;
    private UserFriend testUserFriend1;
    private UserFriend testUserFriend2;
    private List<UserFriend> testUserFriends1;
    private List<UserFriend> testUserFriends2;
    private Picture testPicture1;
    private Picture testPicture2;
    private List<Picture> testPictures;
    private Gallery testGallery;


    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PostRepo postRepo;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    UserFriendRepo userFriendRepo;
    @Autowired
    PictureRepo pictureRepo;
    @Autowired
    GalleryRepo galleryRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {

        testUser1 = userRepo.findByUsername("TestUsername");
        testUser2 = userRepo.findByUsername("TestUsername2");
        testPost = postRepo.findByBody("TestPostBody1");
        testPost2 = postRepo.findByBody("TestPostBody2");
        testUserFriend1 = userFriendRepo.findByUserAndFriend(testUser1, testUser2);
        testUserFriend2 = userFriendRepo.findByUserAndFriend(testUser2, testUser1);
        testComment1 = commentRepo.findByBody("TestCommentBody1");
        testComment2 = commentRepo.findByBody("TestCommentBody2");
        testPicture1 = pictureRepo.findById(1);
        testPicture2 = pictureRepo.findById(2);
        testPictures = pictureRepo.findAllByUser(testUser1);
        testGallery = galleryRepo.findByName("TestGallery1");

//create
        if (testUser1 == null) {
            User newUser = new User();
            newUser.setEmail("TestEmail@Test.com");
            newUser.setUsername("TestUsername");
            newUser.setPassword("TestPassword");
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setFirstName("TestFirstName");
            newUser.setLastName("TestLastName");
            newUser.setAboutMe("TestAboutMe");
            newUser.setBirthday(new Date());
            newUser.setCreatedDate(new Date());
//            newUser.setUserFriends(testUserFriends1);
            testUser1 = userRepo.save(newUser);
        }

        if (testUser2 == null) {
            User newUser2 = new User();
            newUser2.setEmail("TestEmail2@Test.com");
            newUser2.setUsername("TestUsername2");
            newUser2.setPassword("TestPassword2");
            newUser2.setPassword(passwordEncoder.encode(newUser2.getPassword()));
            newUser2.setFirstName("TestFirstName2");
            newUser2.setLastName("TestLastName2");
            newUser2.setAboutMe("TestAboutMe2");
            newUser2.setBirthday(new Date());
            newUser2.setCreatedDate(new Date());
//            newUser.setUserFriends(testUserFriends1);
            testUser2 = userRepo.save(newUser2);
        }

        if (testUserFriend1 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser1);
            newUserFriend.setFriend(testUser2);
            newUserFriend.setStatus(Status.ACCEPTED);
            testUserFriend1 = userFriendRepo.save(newUserFriend);
        }

        if (testUserFriend2 == null) {
            UserFriend newUserFriend2 = new UserFriend();
            newUserFriend2.setUser(testUser2);
            newUserFriend2.setFriend(testUser1);
            newUserFriend2.setStatus(Status.ACCEPTED);
            testUserFriend2 = userFriendRepo.save(newUserFriend2);
        }

        if (testPost == null) {
            Post newPost = new Post();
//            newPost.setId(1);
            newPost.setBody("TestPostBody1");
            newPost.setUser(testUser1);
            newPost.setCreatedDate(new Date());
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("TestPhoto_Url");
            testPost = postRepo.save(newPost);
        }

        if (testPost2 == null) {
            Post newPost2 = new Post();
//            newPost2.setId(2);
            newPost2.setBody("TestPostBody2");
            newPost2.setUser(testUser2);
            newPost2.setCreatedDate(new Date());
            newPost2.setEditDate(new Date());
            newPost2.setPhoto_url("TestPhoto_Url2");
            testPost2 = postRepo.save(newPost2);
        }

        if (testComment1 == null) {
            Comment newComment = new Comment();
            newComment.setParentPost(testPost);
            newComment.setBody("TestCommentBody1");
            newComment.setCreatedDate(new Date());
            newComment.setEditDate(new Date());
            newComment.setPhoto_url("TestPhoto_UrlComment1");
            newComment.setUser(testUser2);
            testComment1 = commentRepo.save(newComment);
        }
        if (testComment2 == null) {
            Comment newComment2 = new Comment();
            newComment2.setParentPost(testPost);
            newComment2.setBody("TestCommentBody2");
            newComment2.setCreatedDate(new Date());
            newComment2.setEditDate(new Date());
            newComment2.setPhoto_url("TestPhoto_UrlComment2");
            newComment2.setUser(testUser2);
            testComment2 = commentRepo.save(newComment2);
        }

        // Throws a Post request to /login and expect a redirection to the home page after being logged in
        httpSession = this.mvc.perform(post("/login").with(csrf())
                .param("username", "TestUsername")
                .param("password", "TestPassword"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();
    }

    //Sanity Check
    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        // It makes sure the returned session is not null
        assertNotNull(httpSession);
    }

    @Test
    public void testShowRegistration() throws Exception {
        this.mvc.perform(get("/sign-up"))
                .andExpect(status().isOk());
    }

    //Create Post
    @Test
    public void testCreatePost() throws Exception {
        User existingUser = userRepo.findAll().get(0);
        String username = existingUser.getUsername();
        this.mvc.perform(
                post("/posts/create/" + username).with(csrf())
                        .param("createdDate", new Date().toString())
                        .param("body", "post test body"))
                .andExpect(status().is3xxRedirection());
    }

    //Read posts
    @Test
    public void showAllPosts() throws Exception {
        User existingUser = userRepo.findAll().get(0);
        Post existingPost = postRepo.findAllByUser(existingUser).get(0);
        this.mvc.perform(get("/posts/view/" + existingUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(existingPost.getBody())));
    }

    //Update Post
    @Test
    public void testEditPost() throws Exception {
        User existingUser = userRepo.findAll().get(0);
        Post existingPost = postRepo.findAllByUser(existingUser).get(0);
        String username = existingUser.getUsername();
        long id = existingPost.getId();
        this.mvc.perform(
                post("/posts/edit/" + username + "/" + id).with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("editDate", new Date().toString())
                        .param("body", "New body"))
                .andExpect(status().is3xxRedirection());
        //checks that updated post information displays when queried
        this.mvc.perform(get("/posts/edit/" + username + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("New body")));
    }

    //Delete Post
    @Test
    public void testDeletePost() throws Exception {
        User existingUser = userRepo.findAll().get(0);
        String username = existingUser.getUsername();
        this.mvc.perform(
                post("/posts/create/" + username).with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("body", "posttestbody"))
                .andExpect(status().is3xxRedirection());
        Post deletingPost = postRepo.findByBody("posttestbody");
        long id = deletingPost.getId();
        this.mvc.perform(
                post("/posts/delete/" + username + "/" + id).with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("id", String.valueOf(deletingPost.getId())))
                .andExpect(status().is3xxRedirection());
    }

    //Create Comment
    @Test
    public void testCreateComment() throws Exception {
        User existingUserParent = userRepo.findAll().get(0);
        User existingUserChild = userRepo.findAll().get(1);
        Post existingParentPost = postRepo.findAllByUser(existingUserParent).get(0);
        this.mvc.perform(
                post(    "/posts/create/" + existingUserChild.getUsername() + "/" + existingParentPost.getId() + "/comment").with(csrf())
                .param("body", "testComment"))
                .andExpect(status().is3xxRedirection());
    }

    //Read NewsFeed comments/posts
    @Test
    public void testReadComment() throws Exception {
        User existingUser = userRepo.findByUsername("TestUsername");
        List<UserFriend> userFriends = userFriendRepo.findAllByUserAndStatus(existingUser, Status.ACCEPTED);// lists friends that you've accepted
        ArrayList<User> displayUsers = new ArrayList<>();// lists User objects of all the users friends
        for (UserFriend userFriend : userFriends) {
            displayUsers.add(userFriend.getFriend());
        }
        displayUsers.add(existingUser);// includes your own posts in stories view
        ArrayList<Post> displayPosts = new ArrayList<>();// lists all posts by all friends and the user
        ArrayList<Comment> displayComments = new ArrayList<>();// lists all comments to all posts by all friends and user
        for (User displayUser : displayUsers) {
            for (Post post : postRepo.findAllByUser(displayUser)) {
                displayPosts.add(post);
                displayComments.addAll(commentRepo.findAllByParentPost(post));
            }
        }

        System.out.println(displayComments);
        System.out.println(displayPosts.get(0).getUser().getUsername());
        this.mvc.perform(get("/" + existingUser.getUsername() + "/stories"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(displayComments.get(0).getBody())))
                .andExpect(content().string(containsString(displayPosts.get(0).getBody())))
                .andExpect(content().string(containsString(displayUsers.get(0).getUsername())));

    }

    //Update Comment
    @Test
    public void testEditComment() throws Exception {
        User existingUser = userRepo.findAll().get(0);
        Comment existingComment = commentRepo.findAllByUser(existingUser).get(0);
        String username = existingUser.getUsername();
        long id = existingComment.getId();
        this.mvc.perform(
                post("/comments/edit/" + username + "/" + id).with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("editDate", new Date().toString())
                        .param("body", "New Comment body"))
                .andExpect(status().is3xxRedirection());
        //checks that updated comment information displays when queried
        this.mvc.perform(get("/comments/edit/" + username + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("New Comment body")));
    }

    //Delete Comment
    @Test
    public void testDeleteComment() throws Exception {
        User existingUserParent = userRepo.findAll().get(0);
        User existingUserChild = userRepo.findAll().get(1);
        Post existingParentPost = postRepo.findAllByUser(existingUserParent).get(0);
        this.mvc.perform(
                post("/posts/create/" + existingUserChild.getUsername() + "/" + existingParentPost.getId() + "/comment").with(csrf())
                        .param("body", "test Temporary Comment"))
                .andExpect(status().is3xxRedirection());
        Comment temporaryComment = commentRepo.findByBody("test Temporary Comment");
        long id = temporaryComment.getId();
        this.mvc.perform(
                post("/comments/delete/" + existingUserChild.getUsername() + "/" + id).with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("id", String.valueOf(temporaryComment.getId())))
                .andExpect(status().is3xxRedirection());
    }
}

