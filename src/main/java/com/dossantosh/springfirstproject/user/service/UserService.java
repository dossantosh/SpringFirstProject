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

    private final UserAuthService userAuthService;

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
    public Set<UserAuth> convertirUsuariosAUserAuth(Collection<User> users) {
        return users.stream().map(u -> {
            UserAuth dto = new UserAuth();
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

    // register
    public void guardarUsuario(User user, User existingUser) {

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }

        LinkedHashSet<Long> rolesId = new LinkedHashSet<>();
        LinkedHashSet<Long> modulesId = new LinkedHashSet<>();
        LinkedHashSet<Long> submodulesId = new LinkedHashSet<>();

        for (Roles rol : user.getRoles()) {
            rolesId.add(rol.getId());
        }
        for (Modules mid : user.getModules()) {
            modulesId.add(mid.getId());
        }
        for (Submodules sub : user.getSubmodules()) {
            submodulesId.add(sub.getId());
        }

        UserAuth userAuth = userAuthService.findByUsername(user.getUsername());

        userAuth.setUsername(user.getUsername());
        userAuth.setEmail(user.getEmail());
        userAuth.setEnabled(user.getEnabled());
        userAuth.setRoles(rolesId);
        userAuth.setModules(modulesId);
        userAuth.setSubmodules(submodulesId);
        userAuthService.saveuserAuth(userAuth);

        saveUser(user);
    }

    public void crearUsuario(User user) {

        LinkedHashSet<Roles> roles = new LinkedHashSet<>();
        LinkedHashSet<Modules> modules = new LinkedHashSet<>();
        LinkedHashSet<Submodules> submodules = new LinkedHashSet<>();

        LinkedHashSet<Long> rolesId = new LinkedHashSet<>();
        LinkedHashSet<Long> modulesId = new LinkedHashSet<>();
        LinkedHashSet<Long> submodulesId = new LinkedHashSet<>();

        LinkedHashSet<String> rolesN = new LinkedHashSet<>();
        LinkedHashSet<String> modulesN = new LinkedHashSet<>();
        LinkedHashSet<String> submodulesN = new LinkedHashSet<>();

        if (user.getUsername().equals("sevas")) {
            rolesId.add(1L);
            modulesId.add(1L);
            submodulesId.add(1L);
        }

        rolesId.add(2L);
        modulesId.add(2L);
        submodulesId.add(2L);

        for (Long rol : rolesId) {

            if (roleService.existById(rol)) {
                Roles role = roleService.findById(rol);
                roles.add(role);
                rolesN.add(role.getName());
            }
        }
        for (Long moduleId : modulesId) {

            if (moduleService.existById(moduleId)) {
                Modules module = moduleService.findById(moduleId);
                modules.add(module);
                modulesN.add(module.getName());
            }
        }
        for (Long submoduleId : submodulesId) {

            if (submoduleService.existById(submoduleId)) {
                Submodules submodule = submoduleService.findById(submoduleId);
                submodules.add(submodule);
                submodulesN.add(submodule.getName());
            }
        }

        if (roles.isEmpty() || modules.isEmpty() || submodules.isEmpty()) {
            return;
        }

        UserAuth userAuth = new UserAuth();

        userAuth.setUsername(user.getUsername());
        userAuth.setEmail(user.getEmail());
        userAuth.setEnabled(user.getEnabled());
        userAuth.setRoles(rolesId);
        userAuth.setModules(modulesId);
        userAuth.setSubmodules(submodulesId);
        userAuthService.saveuserAuth(userAuth);

        Preferences preferences = new Preferences();
        preferences.setUserId(userAuth.getId());
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
        saveUser(user);

        String token = tokenService.createVerificationToken(user);
        tokenService.sendVerificationEmailUser(user.getEmail(), token);
    }
}
