package com.example.springfirstproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.Modules;

@Repository
public interface ModuleRepository extends JpaRepository<Modules, Long> {
    
    Optional<Modules> findById(Long idModule);
}
