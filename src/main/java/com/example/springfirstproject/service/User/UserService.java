package com.example.springfirstproject.service.User;

import java.util.SequencedSet;
import java.util.LinkedHashSet;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.User.User;
import com.example.springfirstproject.repositories.User.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public SequencedSet<User> findAll() {
        return new LinkedHashSet<>(userRepository.findAll());
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
