package com.example.marketspring.services;

import com.example.marketspring.models.Product;
import com.example.marketspring.models.Role;
import com.example.marketspring.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private String dateOfRegister;
    private Role role;
    private Long balance;
    private List<Product> userProducts;
    private boolean blocking;

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getSurname(),
                user.getPassword(),
                user.getDateOfRegister(),
                user.getRole(),
                user.getBalance(),
                user.getUserProducts(),
                user.getBlocking()
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
        if(blocking){
            return false;
        } else {
            return true;
        }
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
