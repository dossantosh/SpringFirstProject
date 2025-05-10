package com.example.springfirstproject.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.repositories.RoleRepository;

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