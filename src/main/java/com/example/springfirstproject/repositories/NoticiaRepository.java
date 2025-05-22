package com.example.springfirstproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.models.Noticias;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticias, Long> {
    
}

