 package com.example.springfirstproject.service.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.User.UserChikito;
import com.example.springfirstproject.repositories.User.UserChikitoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserChikitoService {

    private final UserChikitoRepository userChikitoRepository;

    public UserChikito saveUserChikito(UserChikito userCH) {
        return userChikitoRepository.save(userCH);
    }

    public boolean existsByUsername(String username) {
        return userChikitoRepository.existsByUsername(username);
    }

    public Optional<UserChikito> findByUsername(String username) {
        return userChikitoRepository.findByUsername(username);
    }

    public Set<UserChikito> findAll() {
        return new HashSet<>(userChikitoRepository.findAll());
    }
}
