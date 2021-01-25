package com.example.justfriends;
import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;
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
    private Comment testComment1;
    private Comment testComment2;
    private UserFriend testUserFriend1;
    private UserFriend testUserFriend2;
    private Picture testPicture1;
    private Picture testPicture2;
    private List<Picture> pictures;
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
        testPost = postRepo.findById(1);
        testUserFriend1 = userFriendRepo.findById(1);
        testUserFriend2 = userFriendRepo.findById(2);
        testComment1 = commentRepo.findByBody("TestCommentBody1");
        testComment2 = commentRepo.findByBody("TestCommentBody2");
        testPicture1 = pictureRepo.findByPictureUrl("testPictureUrl");
        testPicture2 = pictureRepo.findByPictureUrl("testPictureUrl2");
        testGallery = galleryRepo.findById(1);

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
            newUser2.setEmail("TestEmail@Test.com2");
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

        if (testUserFriend1 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser1);
            newUserFriend.setFriend(testUser2);
            newUserFriend.setStatus(Status.PENDING);
            testUserFriend1 = userFriendRepo.save(newUserFriend);
        }

        if (testUserFriend2 == null) {
            UserFriend newUserFriend2 = new UserFriend();
            newUserFriend2.setUser(testUser2);
            newUserFriend2.setFriend(testUser1);
            newUserFriend2.setStatus(Status.PENDING);
            testUserFriend2 = userFriendRepo.save(newUserFriend2);
        }

        if (testPost == null) {
            Post newPost = new Post();
            newPost.setBody("TestPostBody");
            newPost.setUser(testUser1);
            newPost.setCreatedDate(new Date());
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("TestPhoto_Url");
//            newPost.setComments(commentRepo.findAllByParentPostComments(testPost.getComments()));
            testPost = postRepo.save(testPost);
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

        if (testPicture1 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("testPictureUrl");
            newPicture.setUser(testUser1);
            newPicture.setComment("testPictureComment");
            testPicture1 = pictureRepo.save(newPicture);

        }
        pictures.add(testPicture1);

        if (testPicture2 == null) {
            Picture newPicture2 = new Picture();
            newPicture2.setPictureUrl("testPictureUrl");
            newPicture2.setUser(testUser1);
            newPicture2.setComment("testPictureComment");
            testPicture2 = pictureRepo.save(newPicture2);
        }
        pictures.add(testPicture2);


        if (testGallery == null) {
            Gallery newGallery = new Gallery();
            newGallery.setCreatedDate(new Date());
            newGallery.setPictures(pictures);
            newGallery.setUser(testUser1);
            newGallery.setId(1);
            testGallery = galleryRepo.save(newGallery);
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


}

