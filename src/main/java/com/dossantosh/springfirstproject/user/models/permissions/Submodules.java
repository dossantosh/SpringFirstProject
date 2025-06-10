package com.dossantosh.springfirstproject.user.models.permissions;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@Table(
    name = "submodules",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "id_module"})
    }
)
public class Submodules implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_submodule")
    private Long id;

    @Column(length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_module")
    private Modules module;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Submodules that = (Submodules) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
