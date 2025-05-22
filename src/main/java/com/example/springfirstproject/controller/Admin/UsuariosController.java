package com.example.springfirstproject.controller.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.springfirstproject.config.Anotaciones.modulo.RequiereModulo;
import com.example.springfirstproject.models.permisos.Modules;
import com.example.springfirstproject.models.permisos.Roles;
import com.example.springfirstproject.models.permisos.Submodules;
import com.example.springfirstproject.models.user.User;
import com.example.springfirstproject.models.user.UserAuth;
import com.example.springfirstproject.service.permisos.ModuleService;
import com.example.springfirstproject.service.permisos.RoleService;
import com.example.springfirstproject.service.permisos.SubmoduleService;
import com.example.springfirstproject.service.user.UserAuthService;
import com.example.springfirstproject.service.user.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@RequiereModulo({ 2L })
public class UsuariosController {

    private final UserAuthService userAuthService;
    private final UserService userService;
    private final RoleService roleService;
    private final ModuleService moduleService;
    private final SubmoduleService submoduleService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());
        if (auth.getName().equals("anonymousUser")) {
            return;
        }

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return;
        }
        model.addAttribute("userAuth", userAuth.get());

        // Ejemplo de módulo necesario (ajustar según lógica de permisos)
        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);
    }

    @GetMapping
    public String mostrarUsuarios(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Model model,
            HttpSession session) {

        addCommonAttributes(model);

        // Convertir cadenas vacías a null
        if (username != null && username.isBlank()) {
            username = null;
        }
        if (email != null && email.isBlank()) {
            email = null;
        }

        // 1. Obtener página de usuarios filtrados
        Page<User> pageResult = userService.findByFilters(id, username, email, page, size);

        // 2. Convertir cada User → UserView (si se desea extraer campos adicionales)

        LinkedHashSet<UserAuth> userViews = new LinkedHashSet<>(pageResult.getContent().stream().map(u -> {
            UserAuth dto = new UserAuth();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setEmail(u.getEmail());
            dto.setEnabled(u.getEnabled());
            return dto;
        }).collect(Collectors.toCollection(LinkedHashSet::new)));

        // 3. Atributos de paginación
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", pageResult.hasNext());
        model.addAttribute("hasPrevious", pageResult.hasPrevious());

        // 4. Listas completas de Roles, Modules y Submodules (para formularios de
        // edición)
        LinkedHashSet<Roles> allRoles = new LinkedHashSet<>(roleService.findAll());
        LinkedHashSet<Modules> allModules = new LinkedHashSet<>(moduleService.findAll());
        LinkedHashSet<Submodules> allSubmodules = new LinkedHashSet<>(submoduleService.findAll());

        // 5. Recuperar “selectedUser” de la sesión
        User selectedUser = (User) session.getAttribute("selectedUser");

        // 6. Agregar atributos al modelo
        model.addAttribute("users", userViews);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("allModules", allModules);
        model.addAttribute("allSubmodules", allSubmodules);
        model.addAttribute("selectedUser", selectedUser);

        // 7. Construir mapa de filtros para conservarlos en la vista
        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("username", username);
        filters.put("email", email);
        model.addAttribute("filters", filters);

        return "usuarios";
    }

    @GetMapping("/seleccionar/{id}")
    public String seleccionarUsuario(@PathVariable Long id, HttpSession session) {
        userService.findById(id)
                .ifPresent(user -> session.setAttribute("selectedUser", user));
        return "redirect:/usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute User user, HttpSession session) {

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            LinkedHashSet<Roles> selectedRoles = new LinkedHashSet<>();
            for (Roles r : user.getRoles()) {
                roleService.findById(r.getId()).ifPresent(selectedRoles::add);
            }
            user.setRoles(selectedRoles);
        }

        if (user.getModules() != null && !user.getModules().isEmpty()) {
            LinkedHashSet<Modules> selectedModules = new LinkedHashSet<>();
            for (Modules m : user.getModules()) {
                moduleService.findById(m.getId()).ifPresent(selectedModules::add);
            }
            user.setModules(selectedModules);
        }

        if (user.getSubmodules() != null && !user.getSubmodules().isEmpty()) {
            LinkedHashSet<Submodules> selectedSubmodules = new LinkedHashSet<>();
            for (Submodules s : user.getSubmodules()) {
                submoduleService.findById(s.getId()).ifPresent(selectedSubmodules::add);
            }
            user.setSubmodules(selectedSubmodules);
        }


        userService.saveUser(user);

        session.removeAttribute("selectedUser");
        return "redirect:/usuarios";
    }

    @GetMapping("/cancelar")
    public String cancelarEdicion(HttpSession session) {
        // Limpiar el seleccionado en sesión
        session.removeAttribute("selectedUser");
        return "redirect:/usuarios";
    }
}
