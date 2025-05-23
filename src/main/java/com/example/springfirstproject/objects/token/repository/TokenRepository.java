package com.example.springfirstproject.objects.token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.objects.token.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

        Optional<Token> findByToken(String username);
        void deleteByToken(String token);
}