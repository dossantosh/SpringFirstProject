package com.example.springfirstproject.service;

import java.util.Optional;
import java.util.List;

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

    public List<Submodules> listarSubmodulo(List<Long> lista){
        return submoduleRepository.findAllById(lista);

    } 
    
    public List<Submodules> listarTodosSubmodulos(){
        return submoduleRepository.findAll();

    } 

}
