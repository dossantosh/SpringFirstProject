package com.example.springfirstproject.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="modules")
public class Modules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name="id_module")
    private Long idModule;

    @Column(unique = true, length = 50, name = "name_module")
    private String nameModule;

    @Column(length = 50, name = "imagen_module")
    private String imagenModule;
}
