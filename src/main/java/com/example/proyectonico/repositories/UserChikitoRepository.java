package com.example.proyectonico.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.proyectonico.models.UserChikito;

@Repository
public interface UserChikitoRepository extends JpaRepository<UserChikito, Long> {
    
    Optional<UserChikito> findById(Long id);
    Optional<UserChikito> findByUsername(String username);
    boolean existsByUsername(String username);

}
