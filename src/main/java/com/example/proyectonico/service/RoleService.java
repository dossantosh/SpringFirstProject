package com.example.proyectonico.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.proyectonico.models.Roles;
import com.example.proyectonico.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Roles> buscarRol(Long id){
        return roleRepository.findById(id);
} 

    public List<Roles> listarRoles(List<Long> lista){
        return roleRepository.findAllById(lista);

} 
    
    public List<Roles> listarTodosRoles(){
        return roleRepository.findAll();

} 

}