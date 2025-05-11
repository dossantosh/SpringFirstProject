package com.example.springfirstproject.service;

import java.util.Optional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Modules;
import com.example.springfirstproject.repositories.ModuleRepository;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

        public Optional<Modules> buscarModules(Long id){
        return moduleRepository.findById(id);
        } 

    public Set<Modules> listarModulos(List<Long> lista){
        return new HashSet<>(moduleRepository.findAllById(lista));
        } 
    
    public Set<Modules> listarTodosModulos() {
        return new HashSet<>(moduleRepository.findAll());
}
}
