package com.example.springfirstproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

        Optional<Token> findByToken(String username);
        void deleteByToken(String token);
}