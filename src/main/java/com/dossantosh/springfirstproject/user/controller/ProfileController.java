package com.dossantosh.springfirstproject.user.controller;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dossantosh.springfirstproject.common.GenericController;
import com.dossantosh.springfirstproject.common.config.annotations.module.RequiereModule;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;
import com.dossantosh.springfirstproject.user.service.UserAuthService;
import com.dossantosh.springfirstproject.user.service.UserService;
import com.dossantosh.springfirstproject.user.service.permissions.ModuleService;
import com.dossantosh.springfirstproject.user.service.permissions.RoleService;
import com.dossantosh.springfirstproject.user.service.permissions.SubmoduleService;

@Controller
@RequiereModule({ 2L })
public class ProfileController extends GenericController {

    private final UserService userService;

    private final RoleService roleService;

    private final ModuleService moduleService;

    private final SubmoduleService submoduleService;

    public ProfileController(UserAuthService userAuthService, UserService userService, RoleService roleService,
            ModuleService moduleService, SubmoduleService submoduleService) {
        super(userAuthService);
        this.userService = userService;
        this.roleService = roleService;
        this.moduleService = moduleService;
        this.submoduleService = submoduleService;
    }

    @GetMapping("/user/profile")
    public String showPerfilPanel(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);

        addPrincipalAttributes(auth, model, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

        model.addAttribute("activeNavLink", "profile");

        User user = userService.findByUsername(auth.getName());

        model.addAttribute("user", user);

        StringBuilder sbRol = new StringBuilder();
        for (Roles rol : user.getRoles()) {
            sbRol.append(rol.getName() + " \n");
        }
        StringBuilder sbMod = new StringBuilder();
        for (Modules mod : user.getModules()) {
            sbMod.append(mod.getName() + " \n");
        }
        StringBuilder sbSub = new StringBuilder();
        for (Submodules sub : user.getSubmodules()) {
            sbSub.append(sub.getName() + " \n");
        }

        model.addAttribute("listaRol", sbRol);
        model.addAttribute("listaMod", sbMod);
        model.addAttribute("listaSub", sbSub);
        return "user/profile";
    }

    @GetMapping("/user/editar") // /user/editar
    public String mostrarFormularioEdicion(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<Long> lecturaMod = new HashSet<>();
        Set<Long> escrituraMod = new HashSet<>();

        Set<Long> lecturaSub = new HashSet<>();
        Set<Long> escrituraSub = new HashSet<>();

        lecturaMod.add(1L);
        escrituraMod.add(1L);
        lecturaSub.add(1L);
        escrituraSub.add(1L);
        
        addPrincipalAttributes(auth, model, lecturaMod, escrituraMod, lecturaSub, escrituraSub);

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
