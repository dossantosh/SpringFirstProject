package com.dossantosh.springfirstproject.perfume.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "perfumes")
public class Perfumes implements Serializable{

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
    private Types tipo;

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

    @Column(length = 150)
    private String image;

    @Version
    @Column(name = "version")
    private Integer version;

    public Perfumes(String string, Brands hermes, float f, float g, String string2, String string3, int i,
            Types aguaDeColonia) {
    }
}
