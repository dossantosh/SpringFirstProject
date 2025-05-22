package com.example.springfirstproject.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.LinkedHashSet;
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
            @RequestParam(value = "rolesSeleccionados", required = false) LinkedHashSet<Long> rolesId,
            @RequestParam(value = "modulosSeleccionados", required = false) LinkedHashSet<Long> modulosId,
            @RequestParam(value = "submodulosSeleccionados", required = false) LinkedHashSet<Long> submodulosId,
            RedirectAttributes flash) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            return null;
        }
        User existingUser = user.get();

        Optional<UserAuth> userAuth = userAuthService.findByUsername(username);
        if (!userAuth.isPresent()) {
            return null;
        }
        UserAuth existingCh = userAuth.get();

        LinkedHashSet<Roles> roles = new LinkedHashSet<>();
        LinkedHashSet<Modules> modulos = new LinkedHashSet<>();
        LinkedHashSet<Submodules> submodulos = new LinkedHashSet<>();

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
        userAuthService.saveuserAuth(existingCh);

        flash.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "redirect:/perfil";
    }
}
