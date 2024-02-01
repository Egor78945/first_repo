package com.example.socialweb.models.entities;


import com.example.socialweb.models.enums.Role;
import com.example.socialweb.models.requestModels.ProfileSettingsModel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@Data
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> friends;
    @Column(name = "isBan")
    private Boolean isBan;
    @Column(name = "isLock")
    private Boolean isLock;
    @Column(name = "isClose")
    private Boolean closeProfile;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> messages;

    public User() {
        communities = new ArrayList<>();
        friends = new ArrayList<>();
        registerDate = new Date(System.currentTimeMillis()).toString();
        messages = new HashSet<>();
        isBan = false;
        isLock = false;
        closeProfile = false;
    }

    public void addFriend(User user) {
        friends.add(user);
        user.getFriends().add(this);
    }

    public void subscribe(Community community) {
        communities.add(community);
        community.getSubscribers().add(this);
    }

    public void removeFriend(User user) {
        friends.remove(user);
        user.getFriends().remove(this);
    }

    public void unsubscribe(Community community) {
        communities.remove(community);
        community.getSubscribers().remove(this);
    }

    public void like(News news) {
        news.getLike().add(this);
    }
    public void unlike(News news){
        news.getLike().remove(this);
    }

    public void changePassword(String password, PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    public void updateProfile(ProfileSettingsModel model) {
        name = model.getName();
        surname = model.getSurname();
        status = model.getStatus();
        email = model.getEmail();
        age = model.getAge();
        country = model.getCountry();
        city = model.getCity();
        closeProfile = model.isCloseProfile();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getSurname(), user.getSurname()) && Objects.equals(getStatus(), user.getStatus()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getCommunities(), user.getCommunities()) && Objects.equals(getAge(), user.getAge()) && Objects.equals(getCity(), user.getCity()) && getRole() == user.getRole() && Objects.equals(getCountry(), user.getCountry()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getRegisterDate(), user.getRegisterDate()) && Objects.equals(getFriends(), user.getFriends()) && Objects.equals(getIsBan(), user.getIsBan()) && Objects.equals(getIsLock(), user.getIsLock()) && Objects.equals(getCloseProfile(), user.getCloseProfile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getStatus(), getEmail(), getCommunities(), getAge(), getCity(), getRole(), getCountry(), getPassword(), getRegisterDate(), getFriends(), getIsBan(), getIsLock(), getCloseProfile());
    }
    public boolean isUser(){
        return role.equals(Role.USER_ROLE);
    }
    public boolean isAdmin(){
        return role.equals(Role.ADMIN_ROLE);
    }
}
