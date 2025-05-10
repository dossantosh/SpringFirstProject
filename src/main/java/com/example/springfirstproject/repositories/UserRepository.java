package com.example.springfirstproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //findAll() ya lo incluye automáticamente
    
    Optional<User> findById(Long idUser);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
/*
@EnableJpaRepositories(
     basePackages = {      
         "io.hypersistence.utils.spring.repository"
     }
)

@Repository
public interface UserRepository extends
    HibernateRepository<Post>,
    JpaRepository<Post, Long>, CustomPostRepository {
 
}

*/