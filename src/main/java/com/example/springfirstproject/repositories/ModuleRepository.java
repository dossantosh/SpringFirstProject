package com.example.springfirstproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.Modules;

@Repository
public interface ModuleRepository extends JpaRepository<Modules, Long> {
    //findAll() ya lo incluye automáticamente

    // Sacamos el modulo por ID
    Optional<Modules> findById(Long idModule);
}
