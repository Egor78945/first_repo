package com.example.headhunter.services;

import com.example.headhunter.models.Role;
import com.example.headhunter.models.User;
import com.example.headhunter.models.Vacancy;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    private String dateOfRegistration;
    private Role role;
    private List<Vacancy> vacancyList;

    public UserDetails build(User user) {
        return new UserDetails(
                this.id = user.getId(),
                this.name = user.getName(),
                this.surname = user.getSurname(),
                this.phoneNumber = user.getPhoneNumber(),
                this.password = user.getPassword(),
                this.dateOfRegistration = user.getDateOfRegistration(),
                this.role = user.getRole(),
                this.vacancyList = user.getVacancyList()
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
        return phoneNumber;
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
