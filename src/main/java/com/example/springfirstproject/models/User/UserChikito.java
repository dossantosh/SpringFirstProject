package com.example.springfirstproject.models.User;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "usersChikito")
public class UserChikito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column(name = "id_userChikito")
    private Long id;

    @Column(unique = true, length = 50)
    private String username;

    @Column
    private Set<Long> roles =  new HashSet<>();

    @Column
    private Set<Long> modules =  new HashSet<>();

    @Column
    private Set<Long> submodules =  new HashSet<>();

}
