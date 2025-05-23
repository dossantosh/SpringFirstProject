package com.example.springfirstproject.permisos.modules.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springfirstproject.permisos.modules.model.Modules;

@Repository
public interface ModuleRepository extends JpaRepository<Modules, Long> {

}
