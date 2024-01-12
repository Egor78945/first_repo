package com.example.socialweb.services;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.Report;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String name;
    private String surname;
    private String status;
    private String email;
    private List<Community> communities;
    private Integer age;
    private String city;
    private Role role;
    private String country;
    private String password;
    private String registerDate;
    private List<User> friends;
    private Boolean isBan;
    private Boolean isLock;
    private Boolean closeProfile;


    public UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                this.id = user.getId(),
                this.name = user.getName(),
                this.surname = user.getSurname(),
                this.status = user.getStatus(),
                this.email = user.getEmail(),
                this.communities = user.getCommunities(),
                this.age = user.getAge(),
                this.city = user.getCity(),
                this.role = user.getRole(),
                this.country = user.getCountry(),
                this.password = user.getPassword(),
                this.registerDate = user.getRegisterDate(),
                this.friends = user.getFriends(),
                this.isBan = user.getIsBan(),
                this.isLock = user.getIsLock(),
                this.closeProfile = user.getCloseProfile()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
