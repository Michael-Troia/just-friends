package com.example.justfriends.Models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.json.Json;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @NotBlank(message = "Please include your first name.")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Please include your last name.")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Please include your email address.")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    @NotBlank(message = "Please include a username.")
    @Column(nullable = false, unique = true)
    private String username;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column
    private String job;

    @Column
    private String profile_picture_url = "/img/blank-profile-picture.png";

    @NotBlank(message = "Please create a password.")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "friend")
    private List<UserFriend> userFriends;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<Picture> pictures;

    public User(){}

    public User(String aboutMe, Date birthday, String email, String firstName, String password,
                String lastName, String job, String profile_picture_url, String username,
                List<UserFriend> userFriends,  List<Picture> pictures, Date createdDate){
        this.birthday = birthday;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile_picture_url = profile_picture_url;
        this.userFriends = userFriends;
        this.job = job;
        this.username = username;
        this.pictures = pictures;
        this.password = password;
        this.aboutMe = aboutMe;
        this.createdDate = createdDate;
    }

    public User(long id, String aboutMe, Date birthday, String email, String firstName, String password,
                String lastName, String job, String profile_picture_url, String username,
                List<UserFriend> userFriends, List <Picture> pictures, Date createdDate){
        this.aboutMe = aboutMe;
        this.birthday = birthday;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile_picture_url = profile_picture_url;
        this.userFriends = userFriends;
        this.job = job;
        this.username = username;
        this.pictures = pictures;
        this.id = id;
        this.password = password;
        this.createdDate = createdDate;
    }

    public User(User copy) {
        aboutMe = copy.aboutMe;
        birthday = copy.birthday;
        email = copy.email;
        firstName = copy.firstName;
        lastName = copy.lastName;
        profile_picture_url = copy.profile_picture_url;
        userFriends = copy.userFriends;
        job = copy.job;
        username = copy.username;
        pictures = copy.pictures;
        id = copy.id;
        password = copy.password;
        createdDate = copy.createdDate;
    }

    public long getId(){return id;}
    public String getAboutMe(){return aboutMe;}
    public Date getBirthday(){return birthday;}
    public String getEmail(){return email;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public String getUsername(){ return username;}
    public String getJob(){return job;}
    public String getProfile_picture_url(){return profile_picture_url;}
    public List<UserFriend> getUserFriends(){return userFriends;}
    public List<Picture> getPictures(){return pictures;}
    public String getPassword(){return password;}
    public Date getCreatedDate(){return createdDate;}

    public void setId(long id){this.id = id;}
    public void setAboutMe(String aboutMe){this.aboutMe = aboutMe;}
    public void setBirthday(Date birthday){this.birthday = birthday;}
    public void setEmail(String email){this.email = email;}
    public void setFirstName(String firstName){this.firstName = firstName;}
    public void setLastName(String lastName){this.lastName = lastName;}
    public void setUsername(String username){ this.username = username;}
    public void setJob(String job){this.job = job;}
    public void setProfile_picture_url(String profile_picture_url){this.profile_picture_url = profile_picture_url;}
    public void setUserFriends(List<UserFriend> userFriends){this.userFriends = userFriends;}
    public void setPictures(List<Picture> pictures){this.pictures = pictures;}
    public void setPassword(String password){this.password = password;}
    public void setCreatedDate(Date createdDate){this.createdDate = createdDate;}


}
