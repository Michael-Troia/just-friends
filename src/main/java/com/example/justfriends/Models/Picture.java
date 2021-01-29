package com.example.justfriends.Models;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String pictureUrl;

    @Column(nullable = true)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    public Picture(){}

    public Picture(String pictureUrl, String comment, User user, Gallery gallery){
        this.pictureUrl = pictureUrl;
        this.comment = comment;
        this.user = user;
        this.gallery = gallery;
    }

    public Picture(long id, String pictureUrl, String comment, User user, Gallery gallery){
        this. id = id;
        this.pictureUrl = pictureUrl;
        this.comment = comment;
        this.user = user;
        this.gallery = gallery;
    }

    public long getId(){return id;}
    public String getPictureUrl(){return pictureUrl;}
    public String getComment(){return comment;}
    public User getUser(){return user;}
    public Gallery getGallery(){return gallery;}

    public void setId(long id){this.id = id;}
    public void setPictureUrl(String pictureUrl){this.pictureUrl = pictureUrl;}
    public void setComment(String comment){this.comment = comment;}
    public void setUser(User user){this.user = user;}
    public void setGallery(Gallery gallery){this.gallery = gallery;}
}
