package com.example.justfriends;

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
    @JoinColumn(name = "user_photos")
    private User user;
}
