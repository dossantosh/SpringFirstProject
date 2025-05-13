package com.example.springfirstproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.models.Modules;
import com.example.springfirstproject.models.Submodules;
import com.example.springfirstproject.models.User;
import com.example.springfirstproject.models.UserChikito;
import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;

import com.example.springfirstproject.service.RoleService;
import com.example.springfirstproject.service.ModuleService;
import com.example.springfirstproject.service.SubmoduleService;
import com.example.springfirstproject.service.UserChikitoService;
import com.example.springfirstproject.service.UserService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Controller
@RequestMapping("/perfil")
@Transactional
@RequiereModulo({ 2L })
public class EditarController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final RoleService roleService;
    @Autowired
    private final ModuleService moduleService;
    @Autowired
    private final SubmoduleService submoduleService;
    @Autowired
    private final UserChikitoService userChikitoService;

    @GetMapping("/editar") // /perfil/editar
    public String mostrarFormularioEdicion(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("chikito", userChikitoService.findByUsername(auth.getName()));

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("user", userService.findByUsername(username));
        model.addAttribute("todosLosRoles", roleService.findAll());
        model.addAttribute("todosLosModulos", moduleService.findAll());
        model.addAttribute("todosLosSubmodulos", submoduleService.findAll());
        return "perfil/editar";
    }

    @PostMapping("/editar") // /perfil/editar
    public String procesarFormularioEdicion(
            @ModelAttribute("user") User userActualizado,
            @RequestParam(value = "rolesSeleccionados", required = false) Set<Long> rolesId,
            @RequestParam(value = "modulosSeleccionados", required = false) Set<Long> modulosId,
            @RequestParam(value = "submodulosSeleccionados", required = false) Set<Long> submodulosId,
            RedirectAttributes flash) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User userExistente = userService.findByUsername(username);
        UserChikito userCh = userChikitoService.findByUsername(username);

        Set<Roles> roles = new HashSet<>();
        Set<Modules> modulos = new HashSet<>();
        Set<Submodules> submodulos = new HashSet<>();

        for (Long rol : rolesId) {
            Optional<Roles> role = roleService.findById(rol);

            if (role.isPresent()) {
                Roles existingRole = role.get();
                roles.add(existingRole);
            }
        }
        for (Long modulo : modulosId) {
            Optional<Modules> module = moduleService.findById(modulo);

            if (module.isPresent()) {
                Modules existingModule = module.get();
                modulos.add(existingModule);
            }
        }
        for (Long submodulo : submodulosId) {
            Optional<Submodules> submodule = submoduleService.findById(submodulo);

            if (submodule.isPresent()) {
                Submodules existingSubmodule = submodule.get();
                submodulos.add(existingSubmodule);
            }
        }

        if (roles.isEmpty() || modulos.isEmpty() || submodulos.isEmpty()) {
            return null;
        }

        userExistente.setUsername(userActualizado.getUsername());
        userExistente.setRoles(roles);
        userExistente.setModules(modulos);
        userExistente.setSubmodules(submodulos);

        userCh.setUsername(userActualizado.getUsername());
        userCh.setRoles(rolesId);
        userCh.setModules(modulosId);
        userCh.setSubmodules(submodulosId);

        userService.saveUser(userExistente);
        userChikitoService.saveUserChikito(userCh);

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "redirect:/perfil";
    }
}
