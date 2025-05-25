package com.example.springfirstproject.objects.perfumes.perfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.objects.perfumes.perfume.models.Perfumes;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfumes, Long> {
        Optional<Perfumes> findByName(String name);

}
