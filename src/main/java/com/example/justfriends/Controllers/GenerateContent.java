package com.example.justfriends.Controllers;

import com.example.justfriends.Models.*;
import com.example.justfriends.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class GenerateContent {
    private ArrayList<Picture> testPictures1;

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


    @GetMapping("/Generate/Users/Test")
    public void setup() throws Exception {
        LocalDate currentdate = LocalDate.now();
        int currentDay = currentdate.getDayOfMonth();
        Month currentMonth = currentdate.getMonth();
        String monthString = currentMonth.toString();
        String formatMonth = Character.toUpperCase(monthString.charAt(0)) + monthString.substring(1).toLowerCase();
        int currentYear = currentdate.getYear();

        User testUser1 = userRepo.findByUsername("GinaCali92");
        User testUser2 = userRepo.findByUsername("IronMaidenFangirl");
        User testUser3 = userRepo.findByUsername("DanWasHere");
        User testUser4 = userRepo.findByUsername("DragonSlayer95");
        Post testPost = postRepo.findByBody("Wouldn't it be lovely to enjoy a week soaking up the culture ?");
        Post testPost2 = postRepo.findByBody("Of all the places to travel, Mexico is at the top of my list.");
        Post testPost3 = postRepo.findByBody("Look at the happy couple!");
        Post testPost4 = postRepo.findByBody("My sister thinks this photo looks candid. lol.");
        Post testPost5 = postRepo.findByBody("The weather was great, but it was nice to be under the AC with my girls");
        Post testPost6 = postRepo.findByBody("Such goofballs");
        UserFriend testUserFriend1 = userFriendRepo.findByUserAndFriend(testUser1, testUser2);//all users are friends with testUser1
        UserFriend testUserFriend2 = userFriendRepo.findByUserAndFriend(testUser1, testUser3);//all users are friends with testUser1
        UserFriend testUserFriend3 = userFriendRepo.findByUserAndFriend(testUser1, testUser4);//all users are friends with testUser1
        UserFriend testUserFriend4 = userFriendRepo.findByUserAndFriend(testUser2, testUser3);//all users are friends with testUser2
        UserFriend testUserFriend5 = userFriendRepo.findByUserAndFriend(testUser2, testUser4);//all users are friends with testUser2
        Comment testComment1 = commentRepo.findByBody("The Pessimist Sees Difficulty In Every Opportunity. The Optimist Sees Opportunity In Every Difficulty.");
        Comment testComment2 = commentRepo.findByBody("People Who Are Crazy Enough To Think They Can Change The World, Are The Ones Who Do.");
        Comment testComment3 = commentRepo.findByBody("Failure Will Never Overtake Me If My Determination To Succeed Is Strong Enough.");
        Comment testComment4 = commentRepo.findByBody("Imagine Your Life Is Perfect In Every Respect; What Would It Look Like?");
        Comment testComment5 = commentRepo.findByBody("They really are!");
        Picture testPicture1 = pictureRepo.findByComment("Wise beyond his years");
        Picture testPicture2 = pictureRepo.findByComment("Such a pretty boy");
        Picture testPicture3 = pictureRepo.findByComment("I can't wait to go back :(");
        Picture testPicture4 = pictureRepo.findByComment("TestPictureComment4(TestDefaultGallery1");
        Gallery testGallery1 = galleryRepo.findByName("cats&dogs");
        Gallery testGallery2 = galleryRepo.findByName("travels");


        if (testUser1 == null) {
            User newUser = new User();
            newUser.setEmail("TestEmail@Test.com");
            newUser.setUsername("GinaCali92");
            newUser.setPassword("TestPassword");
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setFirstName("Gina");
            newUser.setLastName("Lockwood");
            newUser.setProfile_picture_url("/img/girl1.jpg");
            newUser.setJob("Just a friend");
            newUser.setAboutMe("Friendship is born at that moment when one person says to another, ‘What! You too? I thought I was the only one.");
            newUser.setBirthday(new Date());
            newUser.setCreatedDate(new Date());
            testUser1 = userRepo.save(newUser);
        }
        if (testUser2 == null) {
            User newUser2 = new User();
            newUser2.setEmail("TestEmail2@Test.com");
            newUser2.setUsername("IronMaidenFangirl");
            newUser2.setPassword("TestPassword2");
            newUser2.setPassword(passwordEncoder.encode(newUser2.getPassword()));
            newUser2.setFirstName("Ashley");
            newUser2.setLastName("Cruz");
            newUser2.setJob("Just a friend");
            newUser2.setAboutMe("Friendship is born at that moment when one person says to another, ‘What! You too? I thought I was the only one.");
            newUser2.setProfile_picture_url("/img/girl2.jpg");
            newUser2.setBirthday(new Date());
            newUser2.setCreatedDate(new Date());
            testUser2 = userRepo.save(newUser2);
        }
        if (testUser3 == null) {
            User newUser3 = new User();
            newUser3.setEmail("TestEmail3@Test.com");
            newUser3.setUsername("DanWasHere");
            newUser3.setPassword("TestPassword3");
            newUser3.setPassword(passwordEncoder.encode(newUser3.getPassword()));
            newUser3.setFirstName("Daniel");
            newUser3.setLastName("Craig");
            newUser3.setJob("Just a friend");
            newUser3.setAboutMe("Friendship is born at that moment when one person says to another, ‘What! You too? I thought I was the only one.");
            newUser3.setProfile_picture_url("/img/guy1.jpg");
            newUser3.setBirthday(new Date());
            newUser3.setCreatedDate(new Date());
            testUser3 = userRepo.save(newUser3);
        }
        if (testUser4 == null) {
            User newUser4 = new User();
            newUser4.setEmail("TestEmail4@Test.com");
            newUser4.setUsername("DragonSlayer95");
            newUser4.setPassword("TestPassword4");
            newUser4.setPassword(passwordEncoder.encode(newUser4.getPassword()));
            newUser4.setFirstName("Bo");
            newUser4.setLastName("Hurley");
            newUser4.setJob("Just a friend");
            newUser4.setAboutMe("Friendship is born at that moment when one person says to another, ‘What! You too? I thought I was the only one.");
            newUser4.setProfile_picture_url("/img/family1.jpg");
            newUser4.setBirthday(new Date());
            newUser4.setCreatedDate(new Date());
            testUser4 = userRepo.save(newUser4);
            List<User> userList = new ArrayList<>();
            userList.add(testUser1);
            userList.add(testUser2);
            userList.add(testUser3);
            userList.add(testUser4);
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
            newUserFriend2.setUser(testUser1);
            newUserFriend2.setFriend(testUser3);
            newUserFriend2.setStatus(Status.ACCEPTED);
            testUserFriend2 = userFriendRepo.save(newUserFriend2);
        }
        if (testUserFriend3 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser1);
            newUserFriend.setFriend(testUser4);
            newUserFriend.setStatus(Status.ACCEPTED);
            testUserFriend3 = userFriendRepo.save(newUserFriend);
        }
        if (testUserFriend4 == null) {
            UserFriend newUserFriend2 = new UserFriend();
            newUserFriend2.setUser(testUser2);
            newUserFriend2.setFriend(testUser3);
            newUserFriend2.setStatus(Status.ACCEPTED);
            testUserFriend4 = userFriendRepo.save(newUserFriend2);
        }
        if (testUserFriend5 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUser(testUser2);
            newUserFriend.setFriend(testUser4);
            newUserFriend.setStatus(Status.ACCEPTED);
            testUserFriend5 = userFriendRepo.save(newUserFriend);
        }
        if (testPost == null) {
            Post newPost = new Post();
            newPost.setBody("Wouldn't it be lovely to enjoy a week soaking up the culture ?");
            newPost.setUser(testUser1);
            newPost.setCreatedDate(new Date());
            newPost.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("/img/girls3.jpg");
            testPost = postRepo.save(newPost);
        }
        if (testPost2 == null) {
            Post newPost2 = new Post();
            newPost2.setBody("Of all the places to travel, Mexico is at the top of my list.");
            newPost2.setUser(testUser2);
            newPost2.setCreatedDate(new Date());
            newPost2.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newPost2.setEditDate(new Date());
            newPost2.setPhoto_url("/img/friends1.jpg");
            testPost2 = postRepo.save(newPost2);
        }
        if (testPost3 == null) {
            Post newPost = new Post();
            newPost.setBody("Look at the happy couple!");
            newPost.setUser(testUser3);
            newPost.setCreatedDate(new Date());
            newPost.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("/img/couple1.jpg");
            testPost3 = postRepo.save(newPost);
        }
        if (testPost4 == null) {
            Post newPost2 = new Post();
            newPost2.setBody("My sister thinks this photo looks candid. lol.");
            newPost2.setUser(testUser4);
            newPost2.setCreatedDate(new Date());
            newPost2.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newPost2.setEditDate(new Date());
            newPost2.setPhoto_url("/img/girl3.jpg");
            testPost4 = postRepo.save(newPost2);
        }
        if (testPost5 == null) { //user 1 has 2 posts
            Post newPost = new Post();
            newPost.setBody("The weather was great, but it was nice to be under the AC with my girls");
            newPost.setUser(testUser1);
            newPost.setCreatedDate(new Date());
            newPost.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newPost.setEditDate(new Date());
            newPost.setPhoto_url("/img/girls1.jpg");
            testPost5 = postRepo.save(newPost);
        }
        if (testPost6 == null) { //user2 has 2 posts
            Post newPost2 = new Post();
            newPost2.setBody("Such goofballs");
            newPost2.setUser(testUser2);
            newPost2.setCreatedDate(new Date());
            newPost2.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newPost2.setEditDate(new Date());
            newPost2.setPhoto_url("/img/guys1.jpg");
            testPost6 = postRepo.save(newPost2);
        }

        if (testComment1 == null) { //user2 comments on user1's post
            Comment newComment = new Comment();
            newComment.setParentPost(testPost);
            newComment.setBody("The Pessimist Sees Difficulty In Every Opportunity. The Optimist Sees Opportunity In Every Difficulty.");
            newComment.setCreatedDate(new Date());
            newComment.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newComment.setEditDate(new Date());
            newComment.setPhoto_url("/img/friends-poster.jpg");
            newComment.setUser(testUser2);
            testComment1 = commentRepo.save(newComment);
        }
        if (testComment2 == null) { //user2 comments on user1's post
            Comment newComment2 = new Comment();
            newComment2.setParentPost(testPost);
            newComment2.setBody("People Who Are Crazy Enough To Think They Can Change The World, Are The Ones Who Do.");
            newComment2.setCreatedDate(new Date());
            newComment2.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newComment2.setEditDate(new Date());
            newComment2.setPhoto_url("/img/friends-poster.jpg");
            newComment2.setUser(testUser2);
            testComment2 = commentRepo.save(newComment2);
        }
        if (testComment3 == null) { //user 3 comments on user 2's post
            Comment newComment2 = new Comment();
            newComment2.setParentPost(testPost2);
            newComment2.setBody("Failure Will Never Overtake Me If My Determination To Succeed Is Strong Enough.");
            newComment2.setCreatedDate(new Date());
            newComment2.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newComment2.setEditDate(new Date());
            newComment2.setPhoto_url("/img/friends-poster.jpg");
            newComment2.setUser(testUser3);
            testComment3 = commentRepo.save(newComment2);
        }
        if (testComment4 == null) {//user 4 comments on user 3's post
            Comment newComment = new Comment();
            newComment.setParentPost(testPost3);
            newComment.setBody("Imagine Your Life Is Perfect In Every Respect; What Would It Look Like?");
            newComment.setCreatedDate(new Date());
            newComment.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newComment.setEditDate(new Date());
            newComment.setPhoto_url("/img/friends-poster.jpg");
            newComment.setUser(testUser4);
            testComment4 = commentRepo.save(newComment);
        }
        if (testComment5 == null) {//user 4 comments on user 3's post
            Comment newComment = new Comment();
            newComment.setParentPost(testPost6);
            newComment.setBody("They really are!");
            newComment.setCreatedDate(new Date());
            newComment.setDateString(formatMonth + " " + currentDay + ", " + currentYear);
            newComment.setEditDate(new Date());
            newComment.setPhoto_url("/img/friends-poster.jpg");
            newComment.setUser(testUser1);
            testComment5 = commentRepo.save(newComment);
        }

        if (testGallery1 == null) {
            Gallery newGallery = new Gallery();
            newGallery.setName("cats&dogs");
            newGallery.setUser(testUser1);
            newGallery.setCreatedDate(new Date());
            testGallery1 = galleryRepo.save(newGallery);
        }
        if (testGallery2 == null) {
            Gallery newGallery = new Gallery();
            newGallery.setCreatedDate(new Date());
            newGallery.setUser(testUser1);
            newGallery.setName("travels");
            testGallery2 = galleryRepo.save(newGallery);
        }

        if (testPicture1 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("/img/cat.jpg");
            newPicture.setUser(testUser1);
            newPicture.setComment("Wise beyond his years");
            newPicture.setGallery(galleryRepo.findByName("cats&dogs"));
            testPicture1 = pictureRepo.save(newPicture);
        }
        if (testPicture2 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("/img/dog.jpg");
            newPicture.setUser(testUser1);
            newPicture.setComment("Such a pretty boy");
            newPicture.setGallery(galleryRepo.findByName("cats&dogs"));
            testPicture2 = pictureRepo.save(newPicture);
        }
        if (testPicture3 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("/img/mountain.jpg");
            newPicture.setUser(testUser1);
            newPicture.setComment("I can't wait to go back :(");
            newPicture.setGallery(galleryRepo.findByName("travels"));
            testPicture3 = pictureRepo.save(newPicture);
        }
        if (testPicture4 == null) {
            Picture newPicture = new Picture();
            newPicture.setPictureUrl("/img/squirrel.jpg");
            newPicture.setUser(testUser1);
            newPicture.setComment("Red-handed!");
            newPicture.setGallery(galleryRepo.findByName("travels"));
            testPicture4 = pictureRepo.save(newPicture);
        }
    }
}
