package com.example.springfirstproject.objects.preferencias.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "preferencias")
public class Preferencias {

    @Id
    @Column(name = "userId", nullable = false, unique = true)
    private Long userId;

    @Column(name = "email")
    private boolean notificacionesEmail;

    @Column(name = "sms")
    private boolean notificacionesSMS;

    @Column(length = 50)
    private String tema;

    @Column(length = 50)
    private String idioma;
}
