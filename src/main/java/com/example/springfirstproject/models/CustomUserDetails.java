package com.example.springfirstproject.models;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

public class CustomUserDetails implements UserDetails {
    
    @Autowired
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}