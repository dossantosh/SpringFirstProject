package com.example.springfirstproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.VerificationToken;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
}