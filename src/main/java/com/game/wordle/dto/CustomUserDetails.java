package com.game.wordle.dto;

import com.game.wordle.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private String role;

    // Constructor to initialize CustomUserDetails with User entity data
    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Returning role as authority. You can modify this if you need roles or permissions.
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can modify this to implement account expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can modify this to implement account lock logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can modify this to implement credentials expiration logic
    }

    @Override
    public boolean isEnabled() {
        return true; // You can modify this to implement account enabled logic
    }
}
