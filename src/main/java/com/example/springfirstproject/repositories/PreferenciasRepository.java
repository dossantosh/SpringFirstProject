package com.example.springfirstproject.repositories;

import com.example.springfirstproject.models.Preferencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {

    // Puedes buscar por ID de usuario
    Preferencias findByUserId(Long userId);
}