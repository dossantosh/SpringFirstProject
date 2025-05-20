package com.example.springfirstproject.models.Permisos;

import java.util.HashSet;
import java.util.Set;

import com.example.springfirstproject.models.User.User;

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
    @Column(name = "id_module")
    private Long id;

    @Column(unique = true, length = 50, nullable = false)
    private String name;

    @Column(length = 50)
    private String imagen;

    @ManyToMany(mappedBy = "modules")
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modules modules = (Modules) o;
        return Objects.equals(id, modules.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
