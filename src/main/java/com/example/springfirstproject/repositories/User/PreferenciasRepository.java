package com.example.springfirstproject.repositories.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.User.Preferencias;

@Repository
public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {

    // Puedes buscar por ID de usuario
    Preferencias findByUserId(Long userId);
}