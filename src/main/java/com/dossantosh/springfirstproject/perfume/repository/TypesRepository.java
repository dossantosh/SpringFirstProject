package com.dossantosh.springfirstproject.perfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.perfume.models.Types;

@Repository
public interface TypesRepository extends JpaRepository<Types, Long>{
    Optional<Types> findByName(String name);
}
