package com.dossantosh.springfirstproject.user.service;

import java.util.List;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dossantosh.springfirstproject.common.global.events.Audit.AuditService;
import com.dossantosh.springfirstproject.common.global.page.Direction;
import com.dossantosh.springfirstproject.common.global.page.KeysetPage;
import com.dossantosh.springfirstproject.common.security.custom.auth.UserAuth;
import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserAuthProjection;
import com.dossantosh.springfirstproject.common.security.custom.auth.models.UserContextService;

import com.dossantosh.springfirstproject.pref.PreferencesService;
import com.dossantosh.springfirstproject.pref.models.Preferences;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;

import com.dossantosh.springfirstproject.user.repository.UserRepository;

import com.dossantosh.springfirstproject.user.service.objects.TokenService;
import com.dossantosh.springfirstproject.user.service.permissions.ModuleService;
import com.dossantosh.springfirstproject.user.service.permissions.RoleService;
import com.dossantosh.springfirstproject.user.service.permissions.SubmoduleService;

import com.dossantosh.springfirstproject.user.utils.projections.UserDTO;

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

    private final AuditService auditService;

    private final UserContextService userContextService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    public UserAuthProjection findUserAuthByUsername(String username) {
        return userRepository.findUserAuthByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public User findFullUserById(Long id) {
        return userRepository.findFullUserById(id).orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    public KeysetPage<UserDTO> findUsersKeyset(Long id, String username, String email, Long lastId, int limit,
            Direction direction) {
        // Añadimos un extra para saber si hay más resultados
        List<UserDTO> users = userRepository.findByFiltersKeysetWithDirection(id, username, email, lastId, limit + 1,
                direction.name());

        boolean hasNext = users.size() > limit;
        if (hasNext) {
            users.remove(users.size() - 1); // Quitar extra
        }

        // Si es PREVIOUS, los resultados vienen invertidos (DESC), invertimos para
        // mostrar ASC en la UI
        if (direction == Direction.PREVIOUS) {
            Collections.reverse(users);
        }

        Long newLastId = null;
        if (!users.isEmpty()) {
            if (direction == Direction.NEXT) {
                newLastId = users.get(users.size() - 1).getId();
            } else if (direction == Direction.PREVIOUS) {
                newLastId = users.get(0).getId();
            }
        }

        Long newPreviousId = users.isEmpty() ? null : users.get(0).getId();

        return new KeysetPage<>(users, newLastId, newPreviousId, hasNext);
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

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("Usuario con ID " + id + " no encontrado");
        }

        User user = findById(id);
        auditService.logCustomEvent(
                userContextService.getUsername(),
                "USER_DELETED",
                Map.of("id", user.getId(),
                        "username", user.getUsername() != null ? user.getUsername() : "Sin nombre",
                        "email", user.getEmail() != null ? user.getEmail() : "Sin email",
                        "enabled", user.getEnabled() != null ? user.getEnabled() : "Sin estado",
                        "roles",
                        user.getRoles().stream().map(Roles::getName).toList() != null
                                ? user.getRoles().stream().map(Roles::getName).toList()
                                : "Sin roles",
                        "modules",
                        user.getModules().stream().map(Modules::getName).toList() != null
                                ? user.getModules().stream().map(Modules::getName).toList()
                                : "Sin módulos",
                        "submodules",
                        user.getSubmodules().stream().map(Submodules::getName).toList() != null
                                ? user.getSubmodules().stream().map(Submodules::getName).toList()
                                : "Sin submódulos"));

        userRepository.deleteById(id);
    }

    public void modifyUser(User user, User existingUser) {

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

        auditService.logCustomEvent(
                userContextService.getUsername(),
                "USER_MODIFIED",
                Map.of("id", user.getId() != null ? user.getId() : "Sin ID",
                        "username", user.getUsername() != null ? user.getUsername() : "Sin nombre",
                        "email", user.getEmail() != null ? user.getEmail() : "Sin email",
                        "enabled", user.getEnabled() != null ? user.getEnabled() : "Sin estado",
                        "roles",
                        user.getRoles().stream().map(Roles::getName).toList() != null
                                ? user.getRoles().stream().map(Roles::getName).toList()
                                : "Sin roles",
                        "modules",
                        user.getModules().stream().map(Modules::getName).toList() != null
                                ? user.getModules().stream().map(Modules::getName).toList()
                                : "Sin módulos",
                        "submodules",
                        user.getSubmodules().stream().map(Submodules::getName).toList() != null
                                ? user.getSubmodules().stream().map(Submodules::getName).toList()
                                : "Sin submódulos"));
    }

    // register
    public void createUser(User user) {

        Set<Roles> roles = new HashSet<>();
        Set<Modules> modules = new HashSet<>();
        Set<Submodules> submodules = new HashSet<>();

        Set<Long> rolesId = new HashSet<>();
        Set<Long> modulesId = new HashSet<>();
        Set<Long> submodulesId = new HashSet<>();

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

        user.setEnabled(false);
        user.setRoles(roles);
        user.setModules(modules);
        user.setSubmodules(submodules);

        saveUser(user);

        Preferences preferences = new Preferences();
        preferences.setUserId(user.getId());
        preferences.setTema("auto");
        preferences.setIdioma("es");
        preferences.setEmailNotifications(false);
        preferences.setSmsNotifications(false);

        preferencesService.guardarPreferencias(preferences);

        String token = tokenService.createVerificationToken(user);
        tokenService.sendVerificationEmailUser(user.getEmail(), token);

        auditService.logCustomEvent(
                userContextService.getUsername(),
                "USER_CREATED",
                Map.of("id", user.getId(),
                        "username", user.getUsername() != null ? user.getUsername() : "Sin nombre",
                        "email", user.getEmail() != null ? user.getEmail() : "Sin email",
                        "enabled", user.getEnabled() != null ? user.getEnabled() : "Sin estado",
                        "roles",
                        user.getRoles().stream().map(Roles::getName).toList() != null
                                ? user.getRoles().stream().map(Roles::getName).toList()
                                : "Sin roles",
                        "modules",
                        user.getModules().stream().map(Modules::getName).toList() != null
                                ? user.getModules().stream().map(Modules::getName).toList()
                                : "Sin módulos",
                        "submodules",
                        user.getSubmodules().stream().map(Submodules::getName).toList() != null
                                ? user.getSubmodules().stream().map(Submodules::getName).toList()
                                : "Sin submódulos"));
    }

    public Map<String, List<?>> cargarListasFormulario() {
        Map<String, List<?>> map = new HashMap<>();
        map.put("allRoles", new ArrayList<>(roleService.findAll()));
        map.put("allModules", new ArrayList<>(moduleService.findAll()));
        map.put("allSubmodules", new ArrayList<>(submoduleService.findAll()));
        return map;
    }

    @Transactional(readOnly = true)
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

        UserAuth userAuth = new UserAuth();
        userAuth.setId(user.getId());
        userAuth.setUsername(user.getUsername());
        userAuth.setPassword(user.getPassword());
        userAuth.setEnabled(user.getEnabled());
        userAuth.setPreferences(preferencesService.findByUserId(user.getId()));

        LinkedHashSet<String> hashRoles = new LinkedHashSet<>(user.getRoles().stream()
                .map(Roles::getName).toList());
        userAuth.setRoles(hashRoles);

        LinkedHashSet<Long> hashModules = new LinkedHashSet<>(user.getModules().stream()
                .map(Modules::getId).toList());
        userAuth.setModules(hashModules);

        LinkedHashSet<Long> hashSubmodules = new LinkedHashSet<>(user.getSubmodules().stream()
                .map(Submodules::getId).toList());
        userAuth.setSubmodules(hashSubmodules);

        return userAuth;
    }

    public UserAuth mapToUserAuth(UserAuthProjection projection) {

        if (projection == null) {
            return null;
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setId(projection.getId());
        userAuth.setUsername(projection.getUsername());
        userAuth.setPassword(projection.getPassword());
        userAuth.setEnabled(projection.getEnabled());

        LinkedHashSet<String> hashRoles = new LinkedHashSet<>(projection.getRoles());
        userAuth.setRoles(hashRoles);

        LinkedHashSet<Long> hashModules = new LinkedHashSet<>(projection.getModules());
        userAuth.setModules(hashModules);

        LinkedHashSet<Long> hashSubmodules = new LinkedHashSet<>(projection.getSubmodules());
        userAuth.setSubmodules(hashSubmodules);

        Preferences prefs = new Preferences();
        prefs.setUserId(projection.getPreferencesUserId());

        Boolean emailNotif = projection.getPreferencesEmailNotifications();
        prefs.setEmailNotifications(emailNotif != null ? emailNotif : Boolean.FALSE);

        Boolean smsNotif = projection.getPreferencesSmsNotifications();
        prefs.setSmsNotifications(smsNotif != null ? smsNotif : Boolean.FALSE);

        prefs.setTema(projection.getPreferencesTema() != null ? projection.getPreferencesTema() : "defaultTema");
        prefs.setIdioma(
                projection.getPreferencesIdioma() != null ? projection.getPreferencesIdioma() : "defaultIdioma");

        userAuth.setPreferences(prefs);

        return userAuth;
    }
}
