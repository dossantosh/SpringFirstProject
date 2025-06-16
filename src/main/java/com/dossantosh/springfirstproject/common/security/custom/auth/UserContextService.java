package com.dossantosh.springfirstproject.common.security.custom.auth;

import java.util.Set;

public interface UserContextService {
    Long getId();
    String getUsername();
    boolean getEnabled();
    Set<Long> getRolesId();
    Set<Long> getModules();
    Set<Long> getSubmodules();
}