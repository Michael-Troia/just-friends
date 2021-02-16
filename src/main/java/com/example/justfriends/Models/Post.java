package com.example.justfriends.Models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @Column(nullable = false)
    private String dateString;

    @Column(nullable = true)
    private Date editDate;

    @Column(nullable = true)
    private String photo_url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentPost")
    private List<Comment> comments;

    public Post(){}

    public Post(String body, Date createdDate, String photo_url, User user, Date editDate, List<Comment> comments, String dateString){
        this.body = body;
        this.dateString = dateString;
        this.comments = comments;
        this.createdDate = createdDate;
        this.photo_url = photo_url;
        this.user = user;
        this.editDate = editDate;
    }

    public Post(long id, String body, List<Comment> comments , Date editDate, Date createdDate, String photo_url, User user, String dateString){
        this.body = body;
        this.dateString = dateString;
        this.comments = comments;
        this.editDate = editDate;
        this.createdDate = createdDate;
        this.photo_url = photo_url;
        this.user = user;
        this.id = id;
    }

    public String getBody(){ return body;}
    public Date getCreatedDate() {return createdDate;}
    public List<Comment> getComments(){return comments;}
    public String getPhoto_url(){return photo_url;}
    public User getUser(){return user;}
    public long getId(){return id;}
    public Date getEditDate(){return editDate;}
    public String getDateString(){return dateString;}

    public void setBody(String body){ this.body = body;}
    public void setDateString(String dateString){this.dateString = dateString;}
    public void setComments(List<Comment> comments){this.comments = comments;}
    public void setEditDate(Date editDate){this.editDate = editDate;}
    public void setCreatedDate(Date createdDate){this.createdDate = createdDate;}
    public void setPhoto_url(String photo_url){this.photo_url = photo_url;}
    public void setUser(User user){this.user = user;}
    public void setId(long id){this.id = id;}
}