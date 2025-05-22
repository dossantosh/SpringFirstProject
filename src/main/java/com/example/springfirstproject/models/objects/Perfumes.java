package com.example.springfirstproject.models.objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "perfumes")
public class Perfumes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column
    private Long id;

    @Column(unique = true, length = 100)
    private String name;

    @ManyToOne
    private Brands brand;

    @ManyToOne
    @JoinColumn(name = "tipos_id")
    private Tipos tipo;
     
    @Column
    private float price;

    @Column
    private float volume;

    @Column(length = 50)
    private String season;

    @Column(length = 150)
    private String description;
    
    @Column
    private Integer fecha;
}
