package com.dossantosh.springfirstproject.perfume.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class Brands implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column
    private Long id;

    @Column(unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Perfumes> perfumes = new ArrayList<>();

    public Brands(String string) {
    }
}
