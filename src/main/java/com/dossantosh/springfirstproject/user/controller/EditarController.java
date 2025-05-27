package com.dossantosh.springfirstproject.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dossantosh.springfirstproject.common.config.annotation.module.Requieremodule;
import com.dossantosh.springfirstproject.permisos.model.Modules;
import com.dossantosh.springfirstproject.permisos.model.Roles;
import com.dossantosh.springfirstproject.permisos.model.Submodules;
import com.dossantosh.springfirstproject.permisos.service.ModuleService;
import com.dossantosh.springfirstproject.permisos.service.RoleService;
import com.dossantosh.springfirstproject.permisos.service.SubmoduleService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserAuthService;
import com.dossantosh.springfirstproject.user.service.UserService;

import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@Transactional
@Requieremodule({ 2L })
public class EditarController {

    private final UserService userService;

    private final RoleService roleService;

    private final ModuleService moduleService;

    private final SubmoduleService submoduleService;

    private final UserAuthService userAuthService;

    @GetMapping("/user/editar") // /user/editar
    public String mostrarFormularioEdicion(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserAuth userAuth = userAuthService.findByUsername(auth.getName());

        model.addAttribute("userAuth", userAuth);

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUsername(username);

        model.addAttribute("user", user);

        model.addAttribute("todosLosRoles", roleService.findAll());
        model.addAttribute("todosLosmodules", moduleService.findAll());
        model.addAttribute("todosLosSubmodules", submoduleService.findAll());
        return "user/editar";
    }

    @PostMapping("/user/editar") // /user/editar
    public String procesarFormularioEdicion(
            @ModelAttribute("user") User userActualizado,
            @RequestParam(value = "rolesSeleccionados", required = false) Set<Long> rolesId,
            @RequestParam(value = "modulesSeleccionados", required = false) Set<Long> modulesId,
            @RequestParam(value = "submodulesSeleccionados", required = false) Set<Long> submodulesId,
            RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUsername(auth.getName());

        User existingUser = user;
        
        UserAuth userAuth = userAuthService.findByUsername(auth.getName());

        UserAuth existingCh = userAuth;

        LinkedHashSet<Roles> roles = new LinkedHashSet<>();
        LinkedHashSet<Modules> modules = new LinkedHashSet<>();
        LinkedHashSet<Submodules> submodules = new LinkedHashSet<>();

        for (Long rol : rolesId) {

            if (roleService.existById(rol)) {
                Roles role = roleService.findById(rol);
                roles.add(role);
            }
        }
        for (Long moduleId : modulesId) {

            if (moduleService.existById(moduleId)) {
                Modules module = moduleService.findById(moduleId);
                modules.add(module);
            }
        }
        for (Long submoduleId : submodulesId) {

            if (submoduleService.existById(submoduleId)) {
                Submodules submodule = submoduleService.findById(submoduleId);
                submodules.add(submodule);
            }
        }

        if (roles.isEmpty() || modules.isEmpty() || submodules.isEmpty()) {
            return null;
        }

        existingUser.setUsername(userActualizado.getUsername());
        existingUser.setRoles(roles);
        existingUser.setModules(modules);
        existingUser.setSubmodules(submodules);

        existingCh.setUsername(userActualizado.getUsername());
        existingCh.setRoles(rolesId);
        existingCh.setModules(modulesId);
        existingCh.setSubmodules(submodulesId);

        userService.saveUser(existingUser);
        userAuthService.saveuserAuth(existingCh);

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "user/profile";
    }
}
