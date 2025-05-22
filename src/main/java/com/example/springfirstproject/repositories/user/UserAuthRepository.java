package com.example.springfirstproject.repositories.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.user.UserAuth;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    
    Optional<UserAuth> findByUsername(String username);
    boolean existsByUsername(String username);

}
