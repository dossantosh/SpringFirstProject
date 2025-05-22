package com.example.springfirstproject.repositories.objects;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.objects.Perfumes;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfumes, Long> {
        Optional<Perfumes> findByName(String name);

}
