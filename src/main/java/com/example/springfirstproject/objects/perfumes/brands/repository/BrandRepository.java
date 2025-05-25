package com.example.springfirstproject.objects.perfumes.brands.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.objects.perfumes.brands.models.Brands;

@Repository
public interface BrandRepository extends JpaRepository<Brands, Long> {
    Optional<Brands> findByName(String name);
}
