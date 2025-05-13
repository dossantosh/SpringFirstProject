package com.example.springfirstproject.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.springfirstproject.models.Modules;
import com.example.springfirstproject.models.Roles;
import com.example.springfirstproject.models.Submodules;
import com.example.springfirstproject.models.User;
import com.example.springfirstproject.models.UserChikito;
import com.example.springfirstproject.repositories.ModuleRepository;
import com.example.springfirstproject.repositories.RoleRepository;
import com.example.springfirstproject.repositories.SubmoduleRepository;
import com.example.springfirstproject.repositories.UserRepository;
import com.example.springfirstproject.service.UserChikitoService;
import com.example.springfirstproject.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class LoginController {
    
    @Autowired
    private final UserService userService;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final ModuleRepository moduleRepository;

    @Autowired
    private final SubmoduleRepository submoduleRepository;

    @Autowired
    private final UserChikitoService userChikitoService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Asegúrate que este es el nombre exacto de tu plantilla
    }

    @PostMapping("/register")
    @Transactional
    public String registerUser(@Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {
        String respuesta = "redirect:/login?success=Registro+exitoso.+Por+favor+inicia+sesión";

        if (result.hasErrors()) {
            return "register";
        }

        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "register";
        }

        if (!user.getPassword().startsWith("{bcrypt}")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<Roles> roles = new HashSet<>();
        Set<Modules> modulos = new HashSet<>();
        Set<Submodules> submodulos = new HashSet<>();

        Set<Long> rolesId = new HashSet<>();
        Set<Long> modulosId = new HashSet<>();
        Set<Long> submodulosId = new HashSet<>();

        if (user.getUsername().equals("sevas")) {
            rolesId.add(1L);
            modulosId.add(1L);
            submodulosId.add(1L);
        }

        rolesId.add(2L);
        modulosId.add(2L);
        submodulosId.add(2L);

        for (Long rol : rolesId) {
            Optional<Roles> role = roleRepository.findById(rol);

            if (role.isPresent()) {
                Roles existingRole = role.get();
                roles.add(existingRole);
            }
        }
        for (Long modulo : modulosId) {
            Optional<Modules> module = moduleRepository.findById(modulo);

            if (module.isPresent()) {
                Modules existingModule = module.get();
                modulos.add(existingModule);
            }
        }
        for (Long submodulo : submodulosId) {
            Optional<Submodules> submodule = submoduleRepository.findById(submodulo);

            if (submodule.isPresent()) {
                Submodules existingSubmodule = submodule.get();
                submodulos.add(existingSubmodule);
            }
        }

        if (roles.isEmpty() || modulos.isEmpty() || submodulos.isEmpty()) {
            return null;
        }

        user.setRoles(roles);
        user.setModules(modulos);
        user.setSubmodules(submodulos);

        UserChikito userCh = new UserChikito();
        
        userCh.setUsername(user.getUsername());
        userCh.setRoles(rolesId);
        userCh.setModules(modulosId);
        userCh.setSubmodules(submodulosId);

        userService.saveUser(user);
        userChikitoService.saveUserChikito(userCh);
        return respuesta;
    }

}
