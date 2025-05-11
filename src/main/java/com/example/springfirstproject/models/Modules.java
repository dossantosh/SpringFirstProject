package com.example.springfirstproject.models;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="modules")
public class Modules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name="id_module")
    private Long idModule;

    @Column(unique = true, length = 50, name = "name_module", nullable = false)
    private String nameModule;

    @Column(length = 50, name = "imagen_module")
    private String imagenModule;

    @ManyToMany(mappedBy = "modules")
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modules modules = (Modules) o;
        return Objects.equals(idModule, modules.idModule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idModule);
    }
}
