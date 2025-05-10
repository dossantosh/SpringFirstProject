package com.example.springfirstproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.Submodules;

@Repository
public interface SubmoduleRepository extends JpaRepository<Submodules, Long> {
    //findAll() ya lo incluye automáticamente

    //Buscamos un modulo
    Optional<Submodules> findById(Long idSubmodule);

}