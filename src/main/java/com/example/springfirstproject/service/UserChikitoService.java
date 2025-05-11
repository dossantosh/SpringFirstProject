package com.example.springfirstproject.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.UserChikito;
import com.example.springfirstproject.repositories.UserChikitoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserChikitoService {

    @Autowired
    private final UserChikitoRepository userChikitoRepository;

    public UserChikito saveUserChikito(String username, Set<Long> roles, Set<Long> modulos, Set<Long> submodulos) {

        UserChikito userChikito = new UserChikito();

        userChikito.setUsername(username);
        userChikito.setRoles(roles);
        userChikito.setModules(modulos);
        userChikito.setSubmodules(submodulos);

        return userChikitoRepository.save(userChikito);
    }

    public boolean usernameExists(String username) {
        return userChikitoRepository.existsByUsername(username);
    }

    public UserChikito findByUsername(String username) {
        return userChikitoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("UsuarioChikito: " + username + " no encontrado"));
    }

    public Set<UserChikito> findAll() {
        return new HashSet<>(userChikitoRepository.findAll());
    }
}
