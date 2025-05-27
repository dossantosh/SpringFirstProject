package com.dossantosh.springfirstproject.user.repository.permissions;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.user.models.permissions.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    
}