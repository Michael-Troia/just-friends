package com.example.justfriends.Models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "groups_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String about;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(mappedBy = "groupsAMemberOf")
    private List<User> members;

    public Group(){}

    public Group(String name, String about, User owner, List<User> members){
        this.name = name;
        this.about = about;
        this.owner = owner;
        this.members = members;
    }

    public Group(long id, String name, String about, User owner, List<User> members){
        this.name = name;
        this.about = about;
        this.owner = owner;
        this.members = members;
        this.id = id;
    }

    public long getId(){return id;}
    public String getName(){return name;}
    public String getAbout(){return about;}
    public User getOwner(){return owner;}
    public List<User> getMembers(){return members;}

    public void setId(long id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setAbout(String about){this.about = about;}
    public void setOwner(User owner){this.owner = owner;}
    public void setMembers(List<User> members){this.members = members;}

}
