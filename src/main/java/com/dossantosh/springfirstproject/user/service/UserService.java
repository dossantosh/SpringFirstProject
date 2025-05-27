package com.dossantosh.springfirstproject.user.service;

import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.objects.preferences.models.Preferences;
import com.dossantosh.springfirstproject.objects.preferences.service.PreferencesService;
import com.dossantosh.springfirstproject.objects.token.service.TokenService;
import com.dossantosh.springfirstproject.permisos.model.Modules;
import com.dossantosh.springfirstproject.permisos.model.Roles;
import com.dossantosh.springfirstproject.permisos.model.Submodules;
import com.dossantosh.springfirstproject.permisos.service.ModuleService;
import com.dossantosh.springfirstproject.permisos.service.RoleService;
import com.dossantosh.springfirstproject.permisos.service.SubmoduleService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Marca con ID " + id + " no encontrada"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    public LinkedHashSet<User> findAll() {
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

    @PersistenceContext
    private EntityManager em;

    public Page<User> findByFilters(Long id, String username, String email, int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Construcción de la consulta principal (para resultados paginados)
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> userRoot = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(cb.equal(userRoot.get("id"), id));
        }
        if (username != null && !username.isBlank()) {
            predicates.add(cb.like(cb.lower(userRoot.get("username")), "%" + username.toLowerCase() + "%"));
        }
        if (email != null && !email.isBlank()) {
            predicates.add(cb.like(cb.lower(userRoot.get("email")), "%" + email.toLowerCase() + "%"));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(userRoot.get("id")));

        TypedQuery<User> query = em.createQuery(cq);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<User> resultList = query.getResultList();

        // Construcción de la consulta de conteo (para total de registros)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        List<Predicate> countPredicates = new ArrayList<>();
        if (id != null) {
            countPredicates.add(cb.equal(countRoot.get("id"), id));
        }
        if (username != null && !username.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("username")), "%" + username.toLowerCase() + "%"));
        }
        if (email != null && !email.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("email")), "%" + email.toLowerCase() + "%"));
        }

        countQuery.select(cb.count(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = em.createQuery(countQuery).getSingleResult();

        // --- Construcción del Page ---
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(resultList, pageable, total);
    }

    // Usuarios controller
    public LinkedHashSet<UserAuth> convertirUsuariosAUserAuth(Collection<User> users) {
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
    public void guardarUsuarioConPermisos(User user, User existingUser) {
        user.setRoles(user.getRoles().stream()
                .map(r -> roleService.findById(r.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        user.setModules(user.getModules().stream()
                .map(m -> moduleService.findById(m.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        user.setSubmodules(user.getSubmodules().stream()
                .map(s -> submoduleService.findById(s.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }
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
