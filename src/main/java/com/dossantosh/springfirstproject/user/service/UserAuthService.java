package com.dossantosh.springfirstproject.user.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.repository.UserAuthRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;

    public UserAuth saveuserAuth(UserAuth userAtuh) {
        return userAuthRepository.save(userAtuh);
    }

    public boolean existsByUsername(String username) {
        return userAuthRepository.existsByUsername(username);
    }

    public UserAuth findByUsername(String username) {
        return userAuthRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public Set<UserAuth> findAll() {
        return new HashSet<>(userAuthRepository.findAll());
    }
}
