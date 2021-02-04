package com.example.justfriends.Models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "galleries")
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gallery")
    List<Picture> pictures;

    @OneToOne
    User user;

    @Column(nullable = false)
    private Date createdDate;

    @NotBlank(message = "Please enter a name for your gallery.")
    @Column(nullable = false)
    private String name;

    public Gallery(){}

    public Gallery(List<Picture> pictures, User user, Date createdDate, String name){
        this.pictures = pictures;
        this.name = name;
        this.createdDate = createdDate;
        this.user = user;
    }

    public Gallery(long id, List<Picture> pictures, User user, Date createdDate, String name){
        this.pictures = pictures;
        this.name = name;
        this.createdDate = createdDate;
        this.user = user;
        this.id = id;
    }

    public long getId(){return id;}
    public String getName(){return name;}
    public List<Picture> getPictures(){return pictures;}
    public User getUser(){return user;}
    public Date getCreatedDate(){return createdDate;}

    public void setId(long id){this.id = id;}
    public void setPictures(List<Picture> pictures){this.pictures = pictures;}
    public void setName(String name){ this.name = name;}
    public void setUser(User user){this.user = user;}
    public void setCreatedDate(Date createdDate){this.createdDate = createdDate;}
}
