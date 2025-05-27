package com.dossantosh.springfirstproject.objects.token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.objects.token.model.Token;
import com.dossantosh.springfirstproject.user.models.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

        Optional<Token> findByToken(String token);

        void deleteByToken(String token);

        boolean existsByUser(User user);
}