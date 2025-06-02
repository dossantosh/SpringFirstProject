package com.dossantosh.springfirstproject.user.models;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "user_auth")
public class UserAuth implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name = "id_user_auth")
    private Long id;

    @Column(unique = true, length = 50)
    private String username;

    @Column(unique = true, length = 50)
    @Email
    private String email;

    @Column
    private Boolean enabled;

    @Column
    private Set<Long> roles = new LinkedHashSet<>();

    @Column
    private Set<Long> modules = new LinkedHashSet<>();

    @Column
    private Set<Long> submodules = new LinkedHashSet<>();
}
