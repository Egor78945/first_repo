package com.example.socialweb.models.entities;


import com.example.socialweb.models.enums.Role;
import com.example.socialweb.models.requestModels.ProfileSettingsModel;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> friends;
    @Column(name = "isBan")
    private Boolean isBan;
    @Column(name = "isLock")
    private Boolean isLock;
    @Column(name = "isClose")
    private Boolean closeProfile;

    public User() {
        communities = new ArrayList<>();
        friends = new ArrayList<>();
        registerDate = new Date(System.currentTimeMillis()).toString();
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(status, user.status) && Objects.equals(email, user.email) && Objects.equals(communities, user.communities) && Objects.equals(age, user.age) && Objects.equals(city, user.city) && role == user.role && Objects.equals(country, user.country) && Objects.equals(password, user.password) && Objects.equals(registerDate, user.registerDate) && Objects.equals(friends, user.friends) && Objects.equals(isBan, user.isBan) && Objects.equals(isLock, user.isLock) && Objects.equals(closeProfile, user.closeProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, status, email, communities, age, city, role, country, password, registerDate, friends, isBan, isLock, closeProfile);
    }
}
