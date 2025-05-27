package com.dossantosh.springfirstproject.permisos.model;

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
    private Long id;

    @Column(unique = true, length = 50)
    private String name;

    @ManyToOne()
    @JoinColumn(name="id_module")
    private Modules module; 

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Submodules submodules = (Submodules) o;
        return Objects.equals(id, submodules.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}