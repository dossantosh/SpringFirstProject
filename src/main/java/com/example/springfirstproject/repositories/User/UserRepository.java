package com.example.springfirstproject.repositories.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.user.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}