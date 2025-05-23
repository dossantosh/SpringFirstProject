package com.example.springfirstproject.objects.brands.models;

import java.util.LinkedHashSet;
import java.util.Set;

import com.example.springfirstproject.objects.perfumes.models.Perfumes;

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
public class Brands {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column
    private Long id;

    @Column(unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Perfumes> perfumes = new LinkedHashSet<>();

    public Brands(String string) {
    }
}
