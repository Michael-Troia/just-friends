package com.example.justfriends.Models;

import javax.persistence.*;
@Entity
@Table(name = "userfriends")
public class UserFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public UserFriend() {}

    public UserFriend(User user, User friend, Status status){
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    public UserFriend(long id, User user, User friend, Status status){
        this.status = status;
        this.friend = friend;
        this.id = id;
        this.user = user;
    }

    public long getId(){return id;}
    public User getUser(){return user;}
    public User getFriend(){return friend;}
    public Status getStatus(){return status;}

    public void setId(long id){this.id = id;}
    public void setUser(User user){this.user= user;}
    public void setFriend(User friend){this.friend = friend;}
    public void setStatus(Status status){this.status = status;}
}