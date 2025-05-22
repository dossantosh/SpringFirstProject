 package com.example.springfirstproject.service.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.user.UserAuth;
import com.example.springfirstproject.repositories.user.UserAuthRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;

    public UserAuth saveuserAuth(UserAuth userDTO) {
        return userAuthRepository.save(userDTO);
    }

    public boolean existsByUsername(String username) {
        return userAuthRepository.existsByUsername(username);
    }

    public Optional<UserAuth> findByUsername(String username) {
        return userAuthRepository.findByUsername(username);
    }

    public Set<UserAuth> findAll() {
        return new HashSet<>(userAuthRepository.findAll());
    }
}
