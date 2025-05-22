package com.example.springfirstproject.repositories.objects;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.objects.Brands;

@Repository
public interface BrandRepository extends JpaRepository<Brands, Long> {
    Optional<Brands> findByName(String name);
}
