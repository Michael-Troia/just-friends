package com.example.justfriends.Models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = true)
    private String photo_url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parentPost_id")
    private Post parentPost;

//    @ManyToOne
//    @JoinColumn(name = "comment_id")
//    private Comment comment;

    public Comment(){}

    public Comment(String body, Date createdDate, String photo_url, User user, Post parentPost){
        this.body = body;
        this.createdDate = createdDate;
        this.photo_url = photo_url;
        this.user = user;
        this.parentPost = parentPost;
    }

    public Comment(long id, String body, Date createdDate, String photo_url, User user, Post parentPost){
        this.body = body;
        this.createdDate = createdDate;
        this.photo_url = photo_url;
        this.user = user;
        this.id = id;
        this.parentPost = parentPost;
    }

    public String getBody(){ return body;}
    public Date getCreatedDate(){return createdDate;}
    public String getPhoto_url(){return photo_url;}
    public User getUser(){return user;}
    public long getId(){return id;}
    public Post getParentPost(){return parentPost;}

    public void setBody(String body){ this.body = body;}
    public void setCreatedDate(Date createdDate){this.createdDate = createdDate;}
    public void setPhoto_url(String photo_url){this.photo_url = photo_url;}
    public void setUser(User user){this.user = user;}
    public void setId(long id){this.id = id;}
    public void setParentPost(Post parentPost) {this.parentPost = parentPost;}
}