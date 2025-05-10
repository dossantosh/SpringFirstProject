package com.example.proyectonico.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.proyectonico.models.Modules;
import com.example.proyectonico.repositories.ModuleRepository;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

        public Optional<Modules> buscarModules(Long id){
        return moduleRepository.findById(id);
        } 

    public List<Modules> listarModulos(List<Long> lista){
        return moduleRepository.findAllById(lista);

        } 
    
    public List<Modules> listarTodosModulos(){
        return moduleRepository.findAll();

        } 
}
