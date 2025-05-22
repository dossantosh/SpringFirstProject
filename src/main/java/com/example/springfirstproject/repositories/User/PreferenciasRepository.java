package com.example.springfirstproject.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.user.Preferencias;

@Repository
public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {

    Preferencias findByUserId(Long userId);
}