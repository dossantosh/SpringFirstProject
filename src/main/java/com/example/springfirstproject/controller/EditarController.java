package com.example.springfirstproject.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springfirstproject.models.User;
import com.example.springfirstproject.models.Modules;
import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.models.Submodules;

import com.example.springfirstproject.service.ModuleService;
import com.example.springfirstproject.service.RoleService;
import com.example.springfirstproject.service.SubmoduleService;
import com.example.springfirstproject.service.UserService;

import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/perfil")
@Transactional
public class EditarController {

    private final UserService userService;
    private final RoleService roleService;
    private final ModuleService moduleService;
    private final SubmoduleService submoduleService;

    // Constructor‑injection (ya no hace falta @Autowired aquí)
    public EditarController(UserService userService,
            RoleService roleService,
            ModuleService moduleService,
            SubmoduleService submoduleService) {
        this.userService = userService;
        this.roleService =  roleService;
        this.moduleService = moduleService;
        this.submoduleService = submoduleService;
    }

    @GetMapping("/editar") // /perfil/editar
    public String mostrarFormularioEdicion(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        Set<Roles> setRoles = new HashSet<>(roleService.listarTodosRoles());
        Set<Modules> setMod = new HashSet<>(moduleService.listarTodosModulos());
        Set<Submodules> setSub = new HashSet<>(submoduleService.listarTodosSubmodulos());

        System.out.println("User roles: " + user.getRoles());
        System.out.println("User modules: " + user.getModules());
        System.out.println("User submodules: " + user.getSubmodules());

        model.addAttribute("user", user);
        model.addAttribute("todosLosRoles", setRoles);
        model.addAttribute("todosLosModulos", setMod);
        model.addAttribute("todosLosSubmodulos", setSub);
        return "perfil/editar";
    }

    @PostMapping("/editar") // /perfil/editar
    public String procesarFormularioEdicion(
            @ModelAttribute("user") User userActualizado,
            @RequestParam(value = "rolesSeleccionados", required = false) Set<Long> rolesIds,
            @RequestParam(value = "modulosSeleccionados", required = false) Set<Long> modulosIds,
            @RequestParam(value = "submodulosSeleccionados", required = false) Set<Long> submodulosIds,
            RedirectAttributes flash) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userExistente = userService.findByUsername(username);

        userExistente.setUsername(userActualizado.getUsername());

        // Manejo de nulos
        Set<Long> rolIds = (rolesIds != null ? new HashSet<>(rolesIds) : Collections.emptySet());
        Set<Long> modIds = (modulosIds != null ? new HashSet<>(modulosIds) : Collections.emptySet());
        Set<Long> subIds = (submodulosIds != null ? new HashSet<>(submodulosIds) : Collections.emptySet());

        // Asumo que saveUser asigna roles, módulos y submódulos internamente
        userService.saveUser(userExistente, rolIds, modIds, subIds);

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "redirect:/perfil";
    }
}
