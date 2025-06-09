package com.dossantosh.springfirstproject.common.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.repository.UserRepository;
import com.dossantosh.springfirstproject.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Convertir User → UserAuth
        return userService.userToUserAuth(user);
    }
}
