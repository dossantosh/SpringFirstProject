package com.dossantosh.springfirstproject.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.user.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsById(@NonNull Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}