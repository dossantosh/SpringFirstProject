package com.dossantosh.springfirstproject.permisos.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.permisos.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    
}