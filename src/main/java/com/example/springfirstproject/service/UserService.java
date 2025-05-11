package com.example.springfirstproject.service;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Modules;
import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.models.Submodules;
import com.example.springfirstproject.models.User;
import com.example.springfirstproject.repositories.ModuleRepository;
import com.example.springfirstproject.repositories.RoleRepository;
import com.example.springfirstproject.repositories.SubmoduleRepository;
import com.example.springfirstproject.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final ModuleRepository moduleRepository;

    @Autowired
    private final SubmoduleRepository submoduleRepository;

    @Autowired
    private final UserChikitoService userChikitoService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user, Set<Long> idRole, Set<Long> idModule, Set<Long> idSubmodule) {
        
        if (!user.getPassword().startsWith("{bcrypt}")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<Roles> roles = new HashSet<>();
        Set<Modules> modulos = new HashSet<>();
        Set<Submodules> submodulos = new HashSet<>();

        Set<Long> rolesId = new HashSet<>();
        Set<Long> modulosId = new HashSet<>();
        Set<Long> submodulosId = new HashSet<>();

        for (Long rol : idRole) {
            Optional<Roles> role = roleRepository.findById(rol);

            if (role.isPresent()) {
                Roles existingRole = role.get();
                roles.add(existingRole);
                rolesId.add(rol);
            }
        }
        for (Long modulo : idModule) {
            Optional<Modules> module = moduleRepository.findById(modulo);

            if (module.isPresent()) {
                Modules existingModule = module.get();
                modulos.add(existingModule);
                modulosId.add(modulo);
            }
        }
        for (Long submodulo : idSubmodule) {
            Optional<Submodules> submodule = submoduleRepository.findById(submodulo);

            if (submodule.isPresent()) {
                Submodules existingSubmodule = submodule.get();
                submodulos.add(existingSubmodule);
                submodulosId.add(submodulo);
            }
        }

        user.setRoles(roles);
        user.setModules(modulos);
        user.setSubmodules(submodulos);

        if (roles.isEmpty() || modulos.isEmpty() || submodulos.isEmpty()) {
            return null;
        }

        userChikitoService.saveUserChikito(user.getUsername(), rolesId, modulosId, submodulosId);

        return userRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario: " + username + " no encontrado"));
    }

    public Set<User> findAll() {
        return new HashSet<>(userRepository.findAll());
    }
}
