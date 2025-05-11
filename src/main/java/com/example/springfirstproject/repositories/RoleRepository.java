package com.example.springfirstproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    
    Optional<Roles> findById(Long idRole);

}