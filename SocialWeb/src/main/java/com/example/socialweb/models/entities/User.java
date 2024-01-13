package com.example.socialweb.models.entities;


import com.example.socialweb.models.enums.Role;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "status")
    private String status;
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Community> communities;
    @Column(name = "age")
    private Integer age;
    @Column(name = "city")
    private String city;
    @Column(name = "role")
    private Role role;
    @Column(name = "country")
    private String country;
    @Column(name = "password")
    private String password;
    @Column(name = "registerDate")
    private String registerDate;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> friends;
    @Column(name = "isBan")
    private Boolean isBan;
    @Column(name = "isLock")
    private Boolean isLock;
    @Column(name = "isClose")
    private Boolean closeProfile;
    public User(){
        communities = new ArrayList<>();
        friends = new ArrayList<>();
        registerDate = new Date(System.currentTimeMillis()).toString();
        isBan = false;
        isLock = false;
        closeProfile = false;
    }
    public void addFriend(User user){
        friends.add(user);
        user.getFriends().add(this);
    }
    public void subscribe(Community community){
        communities.add(community);
        community.getSubscribers().add(this);
    }
    public void removeFriend(User user){
        friends.remove(user);
        user.getFriends().remove(this);
    }
    public void unsubscribe(Community community){
        communities.remove(community);
        community.getSubscribers().remove(this);
    }
    public void like(News news){
        news.getLike().add(this);
    }
}
