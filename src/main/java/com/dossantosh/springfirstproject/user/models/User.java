package com.dossantosh.springfirstproject.user.models;

import java.util.LinkedHashSet;
import java.util.Set;

import com.dossantosh.springfirstproject.permisos.model.Modules;
import com.dossantosh.springfirstproject.permisos.model.Roles;
import com.dossantosh.springfirstproject.permisos.model.Submodules;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name = "id_user")
    private Long id;

    @Column(unique = true, length = 50)
    private String username;

    @Column(unique = true, length = 50)
    @Email
    private String email;

    @Column
    private Boolean enabled;

    @Column(length = 100)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id_role"))
    private Set<Roles> roles = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_modules", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_module", referencedColumnName = "id_module"))
    private Set<Modules> modules = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_submodules", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_submodule", referencedColumnName = "id_submodule"))
    private Set<Submodules> submodules = new LinkedHashSet<>();
}
