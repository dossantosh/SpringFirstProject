package com.dossantosh.springfirstproject.common.security.custom.auth;

import java.util.List;

public interface UserAuthProjection {
    Long getId();

    String getUsername();

    String getPassword();

    Boolean getEnabled();

    List<String> getRoles();

    List<Long> getModules();

    List<Long> getSubmodules();
}