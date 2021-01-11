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

    public UserFriend() {}

    public UserFriend(User user, User friend){
        this.user = user;
        this.friend = friend;
    }

    public UserFriend(long id, User user, User friend){
        this.id = id;
        this.user = user;
        this.friend = friend;
    }

    public long getId(){return id;}
    public User getUser(){return user;}
    public User getFriend(){return friend;}

    public void setId(long id){this.id = id;}
    public void setUser(User user){this.user= user;}
    public void setFriend(User friend){this.friend = friend;}
}