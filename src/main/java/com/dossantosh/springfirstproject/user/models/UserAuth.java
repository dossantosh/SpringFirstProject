package com.dossantosh.springfirstproject.user.models;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.*;

@Getter
@Setter
public class UserAuth implements Serializable {

    private Long id;

    private String username;

    private Boolean enabled;

    private Set<Long> roles = new LinkedHashSet<>();

    private Set<Long> modules = new LinkedHashSet<>();

    private Set<Long> submodules = new LinkedHashSet<>();
}
