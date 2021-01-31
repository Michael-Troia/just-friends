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
    private User testUser3;
    private User testUser4;
    private Post testPost;
    private Post testPost2;
    private Post testPost3;
    private Post testPost4;
    private Post testPost5;
    private Post testPost6;
    private Comment testComment1;
    private Comment testComment2;
    private Comment testComment3;
    private Comment testComment4;
    private UserFriend testUserFriend1;
    private UserFriend testUserFriend2;
    private UserFriend testUserFriend3;
    private UserFriend testUserFriend4;
    private UserFriend testUserFriend5;
    private UserFriend testUserFriend6;
    private UserFriend testUserFriend7;
    private UserFriend testUserFriend8;
    private UserFriend testUserFriend9;
    private UserFriend testUserFriend10;
    private Picture testPicture1;
    private Picture testPicture2;
    private Picture testPicture3;
    private Picture testPicture4;
    private List<Picture> testPictures1;
    private Gallery testGallery1;
    private Gallery testGallery2;


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
        testUser3 = userRepo.findByUsername("TestUsername3");
        testUser4 = userRepo.findByUsername("TestUsername4");
        testPost = postRepo.findByBody("TestPostBody1");
        testPost2 = postRepo.findByBody("TestPostBody2");
        testPost3 = postRepo.findByBody("TestPostBody3");
        testPost4 = postRepo.findByBody("TestPostBody4");
        testPost5 = postRepo.findByBody("TestPostBody5");
        testPost6 = postRepo.findByBody("TestPostBody6");
        testUserFriend1 = userFriendRepo.findByUserAndFriend(testUser1, testUser2);//all users are friends with testUser1
        testUserFriend2 = userFriendRepo.findByUserAndFriend(testUser2, testUser1);//all users are friends with testUser1
        testUserFriend3 = userFriendRepo.findByUserAndFriend(testUser1, testUser3);//all users are friends with testUser1
        testUserFriend4 = userFriendRepo.findByUserAndFriend(testUser3, testUser1);//all users are friends with testUser1
        testUserFriend5 = userFriendRepo.findByUserAndFriend(testUser1, testUser4);//all users are friends with testUser1
        testUserFriend6 = userFriendRepo.findByUserAndFriend(testUser4, testUser1);//all users are friends with testUser1
        testUserFriend7 = userFriendRepo.findByUserAndFriend(testUser2, testUser3);//all users are friends with testUser2
        testUserFriend8 = userFriendRepo.findByUserAndFriend(testUser3, testUser2);//all users are friends with testUser2
        testUserFriend9 = userFriendRepo.findByUserAndFriend(testUser2, testUser4);//all users are friends with testUser2
        testUserFriend10 = userFriendRepo.findByUserAndFriend(testUser4, testUser2);//all users are friends with testUser2
//        testUserFriend1 = userFriendRepo.findByUserAndFriend(testUser3, testUser4);  testUser3 and testUser4 are not friends
//        testUserFriend2 = userFriendRepo.findByUserAndFriend(testUser4, testUser3);  testUser3 and testUser4 are not friends
        testComment1 = commentRepo.findByBody("TestCommentBody1");
        testComment2 = commentRepo.findByBody("TestCommentBody2");
        testComment3 = commentRepo.findByBody("TestCommentBody3");
        testComment4 = commentRepo.findByBody("TestCommentBody4");
        testPicture1 = pictureRepo.findByPictureUrl("TestPictureUrl1");
        testPicture2 = pictureRepo.findByPictureUrl("TestPictureUrl2");
        testPicture3 = pictureRepo.findByPictureUrl("TestPictureUrl3");
        testPicture4 = pictureRepo.findByPictureUrl("TestPictureUrl4");
        testPictures1 = pictureRepo.findAllByUser(testUser1);
        testGallery1 = galleryRepo.findByName("TestGallery1");
        testGallery2 = galleryRepo.findByName("TestDefaultGallery1");

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
            testUser2 = userRepo.save(newUser2);
        }
        if (testUser3 == null) {
            User newUser3 = new User();
            newUser3.setEmail("TestEmail3@Test.com");
            newUser3.setUsername("TestUsername3");
            newUser3.setPassword("TestPassword3");
            newUser3.setPassword(passwordEncoder.encode(newUser3.getPassword()));
            newUser3.setFirstName("TestFirstName3");
            newUser3.setLastName("TestLastName3");
            newUser3.setAboutMe("TestAboutMe3");
            newUser3.setBirthday(new Date());
            newUser3.setCreatedDate(new Date());
            testUser3 = userRepo.save(newUser3);
        }
        if (testUser4 == null) {
            User newUser4 = new User();
            newUser4.setEmail("TestEmail4@Test.com");
            newUser4.setUsername("TestUsername4");
            newUser4.setPassword("TestPassword4");
            newUser4.setPassword(passwordEncoder.encode(newUser4.getPassword()));
            newUser4.setFirstName("TestFirstName4");
            newUser4.setLastName("TestLastName4");
            newUser4.setAboutMe("TestAboutMe4");
            newUser4.setBirthday(new Date());
            newUser4.setCreatedDate(new Date());
            testUser4 = userRepo.save(newUser4);
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
        if (testUserFriend3 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser1);
            newUserFriend.setFriend(testUser3);
            newUserFriend.setStatus(Status.ACCEPTED);
            testUserFriend3 = userFriendRepo.save(newUserFriend);
        }
        if (testUserFriend4 == null) {
            UserFriend newUserFriend2 = new UserFriend();
            newUserFriend2.setUser(testUser3);
            newUserFriend2.setFriend(testUser1);
            newUserFriend2.setStatus(Status.ACCEPTED);
            testUserFriend4 = userFriendRepo.save(newUserFriend2);
        }
        if (testUserFriend5 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser1);
            newUserFriend.setFriend(testUser4);
            newUserFriend.setStatus(Status.ACCEPTED);
            testUserFriend5 = userFriendRepo.save(newUserFriend);
        }
        if (testUserFriend6 == null) {
            UserFriend newUserFriend2 = new UserFriend();
            newUserFriend2.setUser(testUser4);
            newUserFriend2.setFriend(testUser1);
            newUserFriend2.setStatus(Status.ACCEPTED);
            testUserFriend6 = userFriendRepo.save(newUserFriend2);
        }
        if (testUserFriend7 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser2);
            newUserFriend.setFriend(testUser3);
            newUserFriend.setStatus(Status.ACCEPTED);
            testUserFriend7 = userFriendRepo.save(newUserFriend);
        }
        if (testUserFriend8 == null) {
            UserFriend newUserFriend2 = new UserFriend();
            newUserFriend2.setUser(testUser3);
            newUserFriend2.setFriend(testUser2);
            newUserFriend2.setStatus(Status.ACCEPTED);
            testUserFriend8 = userFriendRepo.save(newUserFriend2);
        }
        if (testUserFriend9 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser2);
            newUserFriend.setFriend(testUser4);
            newUserFriend.setStatus(Status.ACCEPTED);
            testUserFriend9 = userFriendRepo.save(newUserFriend);
        }
        if (testUserFriend10 == null) {
            UserFriend newUserFriend2 = new UserFriend();
            newUserFriend2.setUser(testUser4);
            newUserFriend2.setFriend(testUser2);
            newUserFriend2.setStatus(Status.ACCEPTED);
            testUserFriend10 = userFriendRepo.save(newUserFriend2);
        }

        if (testPost == null) {
            Post newPost = new Post();
            newPost.setBody("TestPostBody1");
            newPost.setUser(testUser1);
            newPost.setCreatedDate(new Date());
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("TestPhoto_Url");
            testPost = postRepo.save(newPost);
        }
        if (testPost2 == null) {
            Post newPost2 = new Post();
            newPost2.setBody("TestPostBody2");
            newPost2.setUser(testUser2);
            newPost2.setCreatedDate(new Date());
            newPost2.setEditDate(new Date());
            newPost2.setPhoto_url("TestPhoto_Url2");
            testPost2 = postRepo.save(newPost2);
        }
        if (testPost3 == null) {
            Post newPost = new Post();
            newPost.setBody("TestPostBody3");
            newPost.setUser(testUser3);
            newPost.setCreatedDate(new Date());
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("TestPhoto_Url3");
            testPost3 = postRepo.save(newPost);
        }
        if (testPost4 == null) {
            Post newPost2 = new Post();
            newPost2.setBody("TestPostBody4");
            newPost2.setUser(testUser4);
            newPost2.setCreatedDate(new Date());
            newPost2.setEditDate(new Date());
            newPost2.setPhoto_url("TestPhoto_Url4");
            testPost4 = postRepo.save(newPost2);
        }
        if (testPost5 == null) { //user 1 has 2 posts
            Post newPost = new Post();
            newPost.setBody("TestPostBody5");
            newPost.setUser(testUser1);
            newPost.setCreatedDate(new Date());
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("TestPhoto_Url5");
            testPost5 = postRepo.save(newPost);
        }
        if (testPost6 == null) { //user2 has 2 posts
            Post newPost2 = new Post();
            newPost2.setBody("TestPostBody6");
            newPost2.setUser(testUser2);
            newPost2.setCreatedDate(new Date());
            newPost2.setEditDate(new Date());
            newPost2.setPhoto_url("TestPhoto_Url6");
            testPost6 = postRepo.save(newPost2);
        }

        if (testComment1 == null) { //user2 comments on user1's post
            Comment newComment = new Comment();
            newComment.setParentPost(testPost);
            newComment.setBody("TestCommentBody1");
            newComment.setCreatedDate(new Date());
            newComment.setEditDate(new Date());
            newComment.setPhoto_url("TestPhoto_UrlComment1");
            newComment.setUser(testUser2);
            testComment1 = commentRepo.save(newComment);
        }
        if (testComment2 == null) { //user2 comments on user1's post
            Comment newComment2 = new Comment();
            newComment2.setParentPost(testPost);
            newComment2.setBody("TestCommentBody2");
            newComment2.setCreatedDate(new Date());
            newComment2.setEditDate(new Date());
            newComment2.setPhoto_url("TestPhoto_UrlComment2");
            newComment2.setUser(testUser2);
            testComment2 = commentRepo.save(newComment2);
        }
        if (testComment3 == null) { //user 3 comments on user 2's post
            Comment newComment2 = new Comment();
            newComment2.setParentPost(testPost2);
            newComment2.setBody("TestCommentBody3");
            newComment2.setCreatedDate(new Date());
            newComment2.setEditDate(new Date());
            newComment2.setPhoto_url("TestPhoto_UrlComment3");
            newComment2.setUser(testUser3);
            testComment3 = commentRepo.save(newComment2);
        }
        if (testComment4 == null) {//user 4 comments on user 3's post
            Comment newComment = new Comment();
            newComment.setParentPost(testPost3);
            newComment.setBody("TestCommentBody4");
            newComment.setCreatedDate(new Date());
            newComment.setEditDate(new Date());
            newComment.setPhoto_url("TestPhoto_UrlComment4");
            newComment.setUser(testUser4);
            testComment1 = commentRepo.save(newComment);
        }

        if (testGallery1 == null) {
            Gallery newGallery = new Gallery();
            newGallery.setName("TestGallery1");
            newGallery.setUser(testUser1);
            newGallery.setCreatedDate(new Date());
            testGallery1 = galleryRepo.save(newGallery);
        }
        if (testGallery2 == null) {
            Gallery newGallery = new Gallery();
            newGallery.setCreatedDate(new Date( ));
            newGallery.setUser(testUser1);
            newGallery.setName("TestDefaultGallery1");
            testGallery2 = galleryRepo.save(newGallery);
        }

        if (testPicture1 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("TestPictureUrl1");
            newPicture.setUser(testUser1);
            newPicture.setComment("TestPictureComment1(TestGallery1)");
            newPicture.setGallery(galleryRepo.findByName("TestGallery1"));
            testPicture1 = pictureRepo.save(newPicture);
        }
        if (testPicture2 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("TestPictureUrl2");
            newPicture.setUser(testUser1);
            newPicture.setComment("TestPictureComment2(TestGallery1)");
            newPicture.setGallery(galleryRepo.findByName("TestGallery1"));
            testPicture2 = pictureRepo.save(newPicture);
        }
        if (testPicture3 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("TestPictureUrl3");
            newPicture.setUser(testUser1);
            newPicture.setComment("TestPictureComment3(TestDefaultGallery1)");
            newPicture.setGallery(galleryRepo.findByName("TestDefaultGallery1"));
            testPicture3 = pictureRepo.save(newPicture);
        }
        if (testPicture4 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("TestPictureUrl4");
            newPicture.setUser(testUser1);
            newPicture.setComment("TestPictureComment4(TestDefaultGallery1");
            newPicture.setGallery(galleryRepo.findByName("TestDefaultGallery1"));
            testPicture4 = pictureRepo.save(newPicture);
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
                        .param("body", "testCreatePostBody"))
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
//        if (postRepo.findByBody("testUpdatedPost") == null) {
//            this.mvc.perform(post("/posts/create/" + userRepo.findAll().get(0)).with(csrf())
//                .param("createdDate", new Date().toString())
//                .param("body", "testTemporaryComment"))
//                .andExpect(status().is3xxRedirection());
//        }
        Post existingPost = postRepo.findAll().get(0);
        String username = existingPost.getUser().getUsername();
        long id = existingPost.getId();
        this.mvc.perform(
                post("/posts/edit/" + username + "/" + id).with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("editDate", new Date().toString())
                        .param("body", "testUpdatedPost"))
                .andExpect(status().is3xxRedirection());
        //checks that updated post information displays when queried
        this.mvc.perform(get("/posts/edit/" + username + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testUpdatedPost")));
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
                .param("body", "testCreateCommentBody"))
                .andExpect(status().is3xxRedirection());
    }

    //Read NewsFeed comments/posts
    @Test
    public void testReadComment() throws Exception {
        User existingUser = userRepo.findByUsername("TestUsername2");
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
        Comment existingComment = commentRepo.findAll().get(0);
        User existingUser = existingComment.getUser();
        String username = existingUser.getUsername();
        long id = existingComment.getId();
        this.mvc.perform(
                post("/comments/edit/" + username + "/" + id).with(csrf())
                        .session((MockHttpSession) httpSession)
                        .param("editDate", new Date().toString())
                        .param("body", "testTemporaryComment"))
                .andExpect(status().is3xxRedirection());
        //checks that updated comment information displays when queried
        this.mvc.perform(get("/comments/edit/" + username + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testTemporaryComment")));
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

    //View my-photos
    @Test
    public void testShowMyPhotos() throws Exception {
        User currentUser = userRepo.findByUsername("TestUsername");
        List<Gallery> userGalleries = galleryRepo.findAllByUser(currentUser);

        this.mvc.perform(get("/" + currentUser.getUsername() + "/my-photos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(userGalleries.get(0).getName())));
    }
}

