package com.dossantosh.springfirstproject.perfume.utils;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerfumeDTO implements Serializable{

    private Long id;

    private String name;

    private String brandName;

    private String tipo;

    private String season;

    private String description;

    private Integer fecha;
}
