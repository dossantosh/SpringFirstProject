package com.dossantosh.springfirstproject.user.utils.projections;

public interface UserProjection {
    Long getId();
    String getUsername();
    String getEmail();
    Boolean getEnabled();
    Boolean getIsAdmin();
}