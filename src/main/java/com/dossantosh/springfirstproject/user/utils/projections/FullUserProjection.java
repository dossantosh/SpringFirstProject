package com.dossantosh.springfirstproject.user.utils.projections;
import java.util.List;

public interface FullUserProjection {

    Long getId();

    String getUsername();

    String getEmail();

    Boolean getEnabled();

    List<String> getRolesIds();

    List<String> getModulesIds();

    List<String> getSubmodulesIds();
}
