package com.example.springfirstproject.service;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Roles> findById(Long id){
        return roleRepository.findById(id);
} 

    public Set<Roles> findAllById(Set<Long> listaId){
        return new HashSet<>(roleRepository.findAllById(listaId));

} 
    
    public Set<Roles> findAll(){
        return new HashSet<>(roleRepository.findAll());

} 

}