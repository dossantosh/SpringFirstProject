package com.dossantosh.springfirstproject.perfume.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "perfumes")
public class Perfumes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Compatible con SERIAL de PostgreSQL
    @Column
    private Long id;

    @Column(unique = true, length = 100)
    private String name;

    @ManyToOne
    private Brands brand;

    @ManyToOne
    @JoinColumn(name = "tipos_id")
    private Types tipo;

    @DecimalMin("10.0")
    @DecimalMax("1000.0")
    @Column
    private Float price;

    @DecimalMin("1.0")
    @DecimalMax("1000.0")
    @Column
    private Float volume;
    
    @Column(length = 50)
    private String season;

    @Size(max = 150)
    @Column(length = 150)
    private String description;

    @Min(1900)
    @Max(2100)
    @Column
    private Integer fecha;

    @Column(length = 150)
    private String image;

    @Version
    @Column(name = "version")
    private Integer version;

    public Perfumes(String string, Brands hermes, Float f, Float g, String string2, String string3, int i,
            Types aguaDeColonia) {
    }
}
