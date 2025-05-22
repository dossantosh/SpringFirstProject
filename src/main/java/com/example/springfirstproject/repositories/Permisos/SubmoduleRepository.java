package com.example.springfirstproject.repositories.Permisos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.permisos.Submodules;

@Repository
public interface SubmoduleRepository extends JpaRepository<Submodules, Long> {

}