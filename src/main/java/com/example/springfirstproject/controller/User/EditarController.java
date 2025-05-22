package com.example.springfirstproject.controller.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springfirstproject.models.Permisos.Modules;
import com.example.springfirstproject.models.Permisos.Roles;
import com.example.springfirstproject.models.Permisos.Submodules;
import com.example.springfirstproject.models.User.User;
import com.example.springfirstproject.models.User.UserChikito;
import com.example.springfirstproject.service.User.UserChikitoService;
import com.example.springfirstproject.service.User.UserService;
import com.example.springfirstproject.service.permisos.ModuleService;
import com.example.springfirstproject.service.permisos.RoleService;
import com.example.springfirstproject.service.permisos.SubmoduleService;
import com.example.springfirstproject.config.Anotaciones.Modulo.RequiereModulo;

import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@Transactional
@RequiereModulo({ 2L })
public class EditarController {

    private final UserService userService;

    private final RoleService roleService;

    private final ModuleService moduleService;

    private final SubmoduleService submoduleService;

    private final UserChikitoService userChikitoService;

    @GetMapping("perfil/editar") // /perfil/editar
    public String mostrarFormularioEdicion(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserChikito> userCh = userChikitoService.findByUsername(auth.getName());
        if (!userCh.isPresent()) {
            return null;
        }
        model.addAttribute("chikito", userCh.get());

        Set<Long> lista = new HashSet<>();
        lista.add(1L);
        model.addAttribute("modulosNecesarios", lista);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            return null;
        }
        model.addAttribute("user", user.get());

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

        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            return null;
        }
        User existingUser = user.get();

        Optional<UserChikito> userCh = userChikitoService.findByUsername(username);
        if (!userCh.isPresent()) {
            return null;
        }
        UserChikito existingCh = userCh.get();

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

        existingUser.setUsername(userActualizado.getUsername());
        existingUser.setRoles(roles);
        existingUser.setModules(modulos);
        existingUser.setSubmodules(submodulos);

        existingCh.setUsername(userActualizado.getUsername());
        existingCh.setRoles(rolesId);
        existingCh.setModules(modulosId);
        existingCh.setSubmodules(submodulosId);

        userService.saveUser(existingUser);
        userChikitoService.saveUserChikito(existingCh);

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "redirect:/perfil";
    }
}
