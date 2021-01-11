package com.example.justfriends.Models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "groupposts")
public class GroupPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = true)
    private String photo_url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    private Group group;

    public GroupPost() {}

    public GroupPost(String body, Date createdDate, String photo_url, User user, Group group){
        this.body = body;
        this.group = group;
        this.createdDate = createdDate;
        this.photo_url = photo_url;
        this.user = user;
    }

    public GroupPost(long id, String body, Date createdDate, String photo_url, User user, Group group){
        this.body = body;
        this.group = group;
        this.createdDate = createdDate;
        this.photo_url = photo_url;
        this.user = user;
        this.id = id;
    }

    public String getBody(){ return body;}
    public Date getCreatedDate(){return createdDate;}
    public String getPhoto_url(){return photo_url;}
    public User getUser(){return user;}
    public long getId(){return id;}
    public Group getGroup(){return group;}

    public void setBody(String body){ this.body = body;}
    public void setCreatedDate(Date createdDate){this.createdDate = createdDate;}
    public void setPhoto_url(String photo_url){this.photo_url = photo_url;}
    public void setUser(User user){this.user = user;}
    public void setId(long id){this.id = id;}
    public void setGroup(Group group){this.group = group;}
}


