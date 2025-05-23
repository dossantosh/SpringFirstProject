package com.example.springfirstproject.permisos.roles.service;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.springfirstproject.permisos.roles.model.Roles;
import com.example.springfirstproject.permisos.roles.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

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