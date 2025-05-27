package com.dossantosh.springfirstproject.user.repository.objects;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.objects.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

        Optional<Token> findByToken(String token);

        void deleteByToken(String token);

        boolean existsByUser(User user);
}