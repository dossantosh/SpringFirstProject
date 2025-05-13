package com.example.springfirstproject.models;

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
@Table(name="noticias")
public class Noticias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name = "id_noticia")
    private Long id;

    @Column(unique = true, length = 50)
    private String titulo;

    @Column(length = 200)
    private String resumen;

    @Column( length = 50)
    private String fecha;

    @Column( length = 50)
    private String imagen;
}
