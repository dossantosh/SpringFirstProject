package com.dossantosh.springfirstproject.user.models.permissions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import com.dossantosh.springfirstproject.user.models.User;

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
@Table(name = "modules")
public class Modules implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name = "id_module")
    private Long id;

    @Column(unique = true, length = 50, nullable = false)
    private String name;

    @Column(length = 50)
    private String image;

    @ManyToMany(mappedBy = "modules")
    private List<User> users = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Modules modules = (Modules) o;
        return Objects.equals(id, modules.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
