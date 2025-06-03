package com.dossantosh.springfirstproject.user.models.objects;

import java.io.Serializable;

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
@Table(name = "preferences")
public class Preferences implements Serializable{

    @Id
    @Column(name = "userId", nullable = false, unique = true)
    private Long userId;

    @Column(name = "Email Notifications")
    private boolean emailNotifications;

    @Column(name = "SMS Notifications")
    private boolean smsNotifications;

    @Column(length = 50)
    private String tema;

    @Column(length = 50)
    private String idioma;
}
