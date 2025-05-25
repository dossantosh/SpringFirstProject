package com.example.springfirstproject.objects.perfumes.perfume.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerfumeDTO {

    private Long id;

    private String name;

    private String brandName;

    private String tipo;

    private String season;

    private String description;

    private Integer fecha;
}
