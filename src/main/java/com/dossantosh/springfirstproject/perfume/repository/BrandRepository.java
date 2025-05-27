package com.dossantosh.springfirstproject.perfume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.perfume.models.Brands;

@Repository
public interface BrandRepository extends JpaRepository<Brands, Long> {
    Optional<Brands> findByName(String name);
}
