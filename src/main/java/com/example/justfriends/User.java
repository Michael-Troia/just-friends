package com.example.justfriends;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column()
    private String aboutMe;

    @Column(nullable = false, unique = true)
    private String username;

    @Column()
    private Date birthday;

    @Column()
    private String job;

    @Column()
    private String profile_picture_url;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "friend")
    private List<UserFriend> userFriends;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Group> groupsOwned;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_groups",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
    private List<Group> groupsAMemberOf;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Picture> pictures;



    public User(){}

    public User(String aboutMe, Date birthday, String email, String firstName,
                String lastName, String job, String profile_picture_url, String username,
                List<UserFriend> userFriends, List<Group> groupsOwned, List<Group> groupsAMemberOf, List<Picture> pictures){
        this.birthday = birthday;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile_picture_url = profile_picture_url;
        this.groupsAMemberOf = groupsAMemberOf;
        this.groupsOwned = groupsOwned;
        this.userFriends = userFriends;
        this.job = job;
        this.username = username;
        this.pictures = pictures;
    }

    public User(long id, String aboutMe, Date birthday, String email, String firstName,
                String lastName, String job, String profile_picture_url, String username,
                List<UserFriend> userFriends, List<Group> groupsOwned, List<Group> groupsAMemberOf, List<Picture> pictures){
        this.aboutMe = aboutMe;
        this.birthday = birthday;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile_picture_url = profile_picture_url;
        this.groupsAMemberOf = groupsAMemberOf;
        this.groupsOwned = groupsOwned;
        this.userFriends = userFriends;
        this.job = job;
        this.username = username;
        this.pictures = pictures;
        this.id = id;
    }


}
