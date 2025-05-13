package com.example.springfirstproject.service;

import java.util.Optional;
import java.util.SequencedSet;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Modules;
import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.models.Submodules;
import com.example.springfirstproject.models.User;
import com.example.springfirstproject.repositories.ModuleRepository;
import com.example.springfirstproject.repositories.RoleRepository;
import com.example.springfirstproject.repositories.SubmoduleRepository;
import com.example.springfirstproject.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario: " + username + " no encontrado"));
    }

    public boolean isPresent(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public SequencedSet<User> findAll() {
        return new LinkedHashSet<>(userRepository.findAll());
    }
}
