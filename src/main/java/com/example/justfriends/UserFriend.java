package com.example.justfriends;

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
}
