package com.example.springfirstproject.objects.preferencias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.objects.preferencias.models.Preferencias;

@Repository
public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {

    Preferencias findByUserId(Long userId);
}