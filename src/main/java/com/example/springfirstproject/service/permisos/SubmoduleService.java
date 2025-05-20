package com.example.springfirstproject.service.permisos;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Permisos.Submodules;
import com.example.springfirstproject.repositories.Permisos.SubmoduleRepository;

@Service
public class SubmoduleService {

    @Autowired
    private SubmoduleRepository submoduleRepository;

    public Optional<Submodules> findById(Long id){
        return submoduleRepository.findById(id);
    } 

    public Set<Submodules> findAllById(Set<Long> listaId){
        return new HashSet<>(submoduleRepository.findAllById(listaId));
    } 
    
    public Set<Submodules> findAll(){
        return new HashSet<>(submoduleRepository.findAll());
    } 

}
