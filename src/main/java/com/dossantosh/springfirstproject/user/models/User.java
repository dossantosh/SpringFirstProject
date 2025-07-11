package com.dossantosh.springfirstproject.user.models;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.dossantosh.springfirstproject.pref.models.Preferences;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Min(1)
    @Max(999999999)
    @Column(name = "id_user")
    private Long id;

    @Size(max = 40)
    @Column(unique = true, length = 50)
    private String username;

    @Email
    @Column(unique = true, length = 50)
    private String email;

    @Column
    private Boolean enabled;

    @Column(length = 100)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Preferences preferences;

    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id_role"))
    private List<Roles> roles = new ArrayList<>();

    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_modules", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_module", referencedColumnName = "id_module"))
    private List<Modules> modules = new ArrayList<>();

    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_submodules", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_submodule", referencedColumnName = "id_submodule"))
    private List<Submodules> submodules = new ArrayList<>();
}
