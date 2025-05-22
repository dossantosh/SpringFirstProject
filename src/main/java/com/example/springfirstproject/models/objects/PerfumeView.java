package com.example.springfirstproject.models.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerfumeView {

    private Long id;

    private String name;

    private String brandName;

    private String tipo;

    private String season;

    private String description;

    private Integer fecha;
}
