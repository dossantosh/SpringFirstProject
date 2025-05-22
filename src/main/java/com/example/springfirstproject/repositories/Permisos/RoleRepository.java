package com.example.springfirstproject.repositories.Permisos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.Permisos.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    
}