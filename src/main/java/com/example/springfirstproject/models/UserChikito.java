package com.example.springfirstproject.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "usersChikito")
public class UserChikito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name = "id_user_chikito")
    private Long idUserChikito;

    @Column(unique = true, length = 50, name = "username")
    private String username;

    @Column(name = "roles")
    private Set<Long> roles =  new HashSet<>();

    @Column(name = "modulos")
    private Set<Long> modules =  new HashSet<>();

    @Column(name = "submodules")
    private Set<Long> submodules =  new HashSet<>();

}
