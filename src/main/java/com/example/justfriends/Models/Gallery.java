package com.example.justfriends.Models;

import javax.persistence.*;
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

    public Gallery(){}

}
