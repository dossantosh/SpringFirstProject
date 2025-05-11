package com.example.springfirstproject.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="submodules")
public class Submodules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name="id_submodule")
    private Long idSubmodule;

    @Column(unique = true, length = 50, name = "name_submodule")
    private String nameSubmodule;

    @ManyToOne()
    @JoinColumn(name="id_module")
    private Modules module; 

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Submodules submodules = (Submodules) o;
        return Objects.equals(idSubmodule, submodules.idSubmodule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSubmodule);
    }
}