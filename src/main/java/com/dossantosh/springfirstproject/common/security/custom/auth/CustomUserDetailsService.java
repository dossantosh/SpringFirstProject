package com.dossantosh.springfirstproject.common.security.custom.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dossantosh.springfirstproject.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Transactional(readOnly = true) // <-- evitar LazyInitializationException
    @Override
    public UserDetails loadUserByUsername(String username) {

        UserAuthProjection userAuthProjection = username == null ? null : userService.findUserAuthByUsername(username);

        return userService.mapToUserAuth(userAuthProjection);
    }
}