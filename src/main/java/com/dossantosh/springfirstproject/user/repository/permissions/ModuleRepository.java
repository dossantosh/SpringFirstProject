package com.dossantosh.springfirstproject.user.repository.permissions;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.user.models.permissions.Modules;

@Repository
public interface ModuleRepository extends JpaRepository<Modules, Long> {
    
}
