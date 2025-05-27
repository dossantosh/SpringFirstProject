package com.dossantosh.springfirstproject.perfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.perfume.models.Perfumes;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfumes, Long>, JpaSpecificationExecutor<Perfumes> {
        Optional<Perfumes> findByName(String name);

}
