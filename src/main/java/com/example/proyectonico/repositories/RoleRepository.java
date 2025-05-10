package com.example.proyectonico.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.proyectonico.models.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    //findAll() ya lo incluye automáticamente

    // Sacamos el rol por ID
    Optional<Roles> findById(Long idRole);

}