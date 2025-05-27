package com.dossantosh.springfirstproject.news.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.news.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    
}

