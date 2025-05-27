package com.dossantosh.springfirstproject.permisos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dossantosh.springfirstproject.permisos.model.Submodules;

@Repository
public interface SubmoduleRepository extends JpaRepository<Submodules, Long> {

}