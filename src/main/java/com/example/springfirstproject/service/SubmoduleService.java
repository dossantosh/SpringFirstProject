package com.example.springfirstproject.service;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Submodules;
import com.example.springfirstproject.repositories.SubmoduleRepository;

@Service
public class SubmoduleService {

    @Autowired
    private SubmoduleRepository submoduleRepository;

    public Optional<Submodules> buscarModulo(Long id){
        return submoduleRepository.findById(id);
    } 

    public Set<Submodules> listarSubmodulo(Set<Long> listaId){
        return new HashSet<>(submoduleRepository.findAllById(listaId));
    } 
    
    public Set<Submodules> listarTodosSubmodulos(){
        return new HashSet<>(submoduleRepository.findAll());
    } 

}
