package com.example.springfirstproject.repositories.objects;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.objects.Tipos;

@Repository
public interface TipoRepository extends JpaRepository<Tipos, Long>{
    Optional<Tipos> findByName(String name);
}
