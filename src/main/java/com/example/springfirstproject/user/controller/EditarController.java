package com.example.springfirstproject.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springfirstproject.common.config.anotaciones.module.Requieremodule;
import com.example.springfirstproject.permisos.modules.model.Modules;
import com.example.springfirstproject.permisos.modules.service.ModuleService;
import com.example.springfirstproject.permisos.roles.model.Roles;
import com.example.springfirstproject.permisos.roles.service.RoleService;
import com.example.springfirstproject.permisos.submodules.model.Submodules;
import com.example.springfirstproject.permisos.submodules.service.SubmoduleService;
import com.example.springfirstproject.user.models.User;
import com.example.springfirstproject.user.models.UserAuth;
import com.example.springfirstproject.user.service.UserAuthService;
import com.example.springfirstproject.user.service.UserService;

import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
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

    @GetMapping("perfil/editar") // /perfil/editar
    public String mostrarFormularioEdicion(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return null;
        }
        model.addAttribute("userAuth", userAuth.get());

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulesNecesarios", lista);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            return null;
        }
        model.addAttribute("user", user.get());

        model.addAttribute("todosLosRoles", roleService.findAll());
        model.addAttribute("todosLosmodules", moduleService.findAll());
        model.addAttribute("todosLosSubmodules", submoduleService.findAll());
        return "perfil/editar";
    }

    @PostMapping("perfil/editar") // /perfil/editar
    public String procesarFormularioEdicion(
            @ModelAttribute("user") User userActualizado,
            @RequestParam(value = "rolesSeleccionados", required = false) LinkedHashSet<Long> rolesId,
            @RequestParam(value = "modulesSeleccionados", required = false) LinkedHashSet<Long> modulesId,
            @RequestParam(value = "submodulesSeleccionados", required = false) LinkedHashSet<Long> submodulesId,
            RedirectAttributes flash) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userService.findByUsername(auth.getName());
        if (!user.isPresent()) {
            return null;
        }
        User existingUser = user.get();
        System.out.println(existingUser.getPassword());
        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return null;
        }
        UserAuth existingCh = userAuth.get();

        LinkedHashSet<Roles> roles = new LinkedHashSet<>();
        LinkedHashSet<Modules> modules = new LinkedHashSet<>();
        LinkedHashSet<Submodules> submodules = new LinkedHashSet<>();

        for (Long rol : rolesId) {
            Optional<Roles> role = roleService.findById(rol);

            if (role.isPresent()) {
                Roles existingRole = role.get();
                roles.add(existingRole);
            }
        }
        for (Long moduleId : modulesId) {
            Optional<Modules> module = moduleService.findById(moduleId);

            if (module.isPresent()) {
                Modules existingModule = module.get();
                modules.add(existingModule);
            }
        }
        for (Long submoduleId : submodulesId) {
            Optional<Submodules> submodule = submoduleService.findById(submoduleId);

            if (submodule.isPresent()) {
                Submodules existingSubmodule = submodule.get();
                submodules.add(existingSubmodule);
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
        return "redirect:/perfil";
    }
}
