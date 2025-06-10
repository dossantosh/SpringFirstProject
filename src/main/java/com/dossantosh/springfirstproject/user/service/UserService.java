package com.dossantosh.springfirstproject.user.service;

import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.models.objects.Preferences;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;
import com.dossantosh.springfirstproject.user.repository.UserRepository;
import com.dossantosh.springfirstproject.user.service.objects.PreferencesService;
import com.dossantosh.springfirstproject.user.service.objects.TokenService;
import com.dossantosh.springfirstproject.user.service.permissions.ModuleService;
import com.dossantosh.springfirstproject.user.service.permissions.RoleService;
import com.dossantosh.springfirstproject.user.service.permissions.SubmoduleService;
import com.dossantosh.springfirstproject.user.utils.UserDTO;
import com.dossantosh.springfirstproject.user.utils.UserSpecifications;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final ModuleService moduleService;

    private final SubmoduleService submoduleService;

    private final PreferencesService preferencesService;

    private final TokenService tokenService;

    public User saveUser(User user) {

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    public Set<User> findAll() {
        return new LinkedHashSet<>(userRepository.findAll());
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Page<User> findByFilters(Long id, String username, String email, int page, int size, String sortby,
            String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortby));

        Specification<User> spec = UserSpecifications.filter(id, username, email);

        return userRepository.findAll(spec, pageable);
    }

    // Usuarios controller
    public Set<UserDTO> convertirUsuariosADTO(Collection<User> users) {
        return users.stream().map(u -> {
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setEmail(u.getEmail());
            dto.setEnabled(u.getEnabled());
            return dto;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Map<String, Set<?>> cargarListasFormulario() {
        Map<String, Set<?>> map = new HashMap<>();
        map.put("allRoles", new LinkedHashSet<>(roleService.findAll()));
        map.put("allModules", new LinkedHashSet<>(moduleService.findAll()));
        map.put("allSubmodules", new LinkedHashSet<>(submoduleService.findAll()));
        return map;
    }

    public void guardarUsuario(User user, User existingUser) {

        if (user.getId() == null) {
            user.setId(existingUser.getId());
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(existingUser.getEmail());
        }

        if (user.getEnabled() == null) {
            user.setEnabled(existingUser.getEnabled());
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }

        if (user.getRoles() == null || user.getModules() == null || user.getSubmodules() == null) {
            return;
        }

        if (user.getRoles().isEmpty() || user.getModules().isEmpty() || user.getSubmodules().isEmpty()) {
            return;
        }

        saveUser(user);
    }

    // register
    public void crearUsuario(User user) {

        LinkedHashSet<Roles> roles = new LinkedHashSet<>();
        LinkedHashSet<Modules> modules = new LinkedHashSet<>();
        LinkedHashSet<Submodules> submodules = new LinkedHashSet<>();

        LinkedHashSet<Long> rolesId = new LinkedHashSet<>();
        LinkedHashSet<Long> modulesId = new LinkedHashSet<>();
        LinkedHashSet<Long> submodulesId = new LinkedHashSet<>();

        if (user.getUsername().equals("sevas")) {

            rolesId.add(2L);

            modulesId.add(2L);
            modulesId.add(3L);

            submodulesId.add(2L);

            submodulesId.add(3L);
            submodulesId.add(4L);

            submodulesId.add(5L);
            submodulesId.add(6L);
        }

        if (user.getUsername().equals("dossantosh")) {

            modulesId.add(2L);
            modulesId.add(3L);

            submodulesId.add(2L);

            submodulesId.add(3L);

            submodulesId.add(5L);
            submodulesId.add(6L);
        }

        if (user.getUsername().equals("userprueba")) {

            submodulesId.add(5L);

        }

        rolesId.add(1L);
        modulesId.add(1L);
        submodulesId.add(1L);

        Roles role = null;
        for (Long rol : rolesId) {

            if (roleService.existById(rol)) {
                role = roleService.findById(rol);
                roles.add(role);
            }
        }
        Modules module = null;
        for (Long moduleId : modulesId) {

            if (moduleService.existById(moduleId)) {
                module = moduleService.findById(moduleId);
                modules.add(module);
            }
        }
        Submodules submodule = null;
        for (Long submoduleId : submodulesId) {

            if (submoduleService.existById(submoduleId)) {
                submodule = submoduleService.findById(submoduleId);
                submodules.add(submodule);
            }
        }

        if (roles.isEmpty() || modules.isEmpty() || submodules.isEmpty()) {
            return;
        }

        saveUser(user);

        Preferences preferences = new Preferences();
        preferences.setUserId(user.getId());
        preferences.setTema("auto");
        preferences.setIdioma("es");
        preferences.setEmailNotifications(false);
        preferences.setSmsNotifications(false);
        preferencesService.guardarPreferencias(preferences);

        user.setEnabled(false);
        user.setRoles(roles);
        user.setModules(modules);
        user.setSubmodules(submodules);

        preferencesService.guardarPreferencias(preferences);

        String token = tokenService.createVerificationToken(user);
        tokenService.sendVerificationEmailUser(user.getEmail(), token);
    }

    public UserAuth userToUserAuth(User user) {

        if (user.getId() == null) {
            return null;
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return null;
        }

        if (user.getEnabled() == null) {
            return null;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return null;
        }

        if (user.getRoles() == null || user.getModules() == null || user.getSubmodules() == null) {
            return null;
        }

        if (user.getRoles().isEmpty() || user.getModules().isEmpty() || user.getSubmodules().isEmpty()) {
            return null;
        }

        // Convertir User → UserAuth
        UserAuth userAuth = new UserAuth();
        userAuth.setId(user.getId());
        userAuth.setUsername(user.getUsername());
        userAuth.setPassword(user.getPassword());
        userAuth.setEnabled(user.getEnabled());
        userAuth.setPreferences(preferencesService.findByUserId(user.getId()));
        userAuth.setRoles(user.getRoles().stream().map(r -> r.getId()).collect(Collectors.toSet()));
        userAuth.setModules(user.getModules().stream().map(m -> m.getId()).collect(Collectors.toSet()));
        userAuth.setSubmodules(user.getSubmodules().stream().map(s -> s.getId()).collect(Collectors.toSet()));

        return userAuth;
    }
}
