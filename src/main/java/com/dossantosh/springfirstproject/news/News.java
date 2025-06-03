package com.dossantosh.springfirstproject.news;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="news")
public class News implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name = "id_news")
    private Long id;

    @Column(unique = true, length = 50)
    private String tittle;

    @Column(length = 200)
    private String summary;

    @Column( length = 50)
    private String date;

    @Column( length = 50)
    private String image;
}
