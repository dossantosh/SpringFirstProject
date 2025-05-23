package com.example.springfirstproject.objects.tipo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.objects.tipo.model.Tipos;

@Repository
public interface TipoRepository extends JpaRepository<Tipos, Long>{
    Optional<Tipos> findByName(String name);
}
