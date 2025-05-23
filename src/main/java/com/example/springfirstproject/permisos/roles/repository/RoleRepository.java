package com.example.springfirstproject.permisos.roles.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.permisos.roles.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    
}