package com.example.proyectonico.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.proyectonico.models.User;
import com.example.proyectonico.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@Controller
public class LoginController {
    @Autowired
    private final UserService userService;

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

        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "register";
        }

        Set<Long> roles = new HashSet<>();
        Set<Long> modulos = new HashSet<>();
        Set<Long> submodulos = new HashSet<>();

        if (user.getUsername().equals("sevas")) {
            roles.add(1L);
            modulos.add(1L);
            submodulos.add(1L);
            roles.add(2L);
            modulos.add(2L);
            submodulos.add(2L);

            userService.saveUser(user, roles, modulos, submodulos);
            return respuesta;
        }
        if (user.getUsername().equals("dossantosh")) {
            roles.add(2L);
            modulos.add(2L);
            submodulos.add(2L);

            userService.saveUser(user, roles, modulos, submodulos);
            return respuesta;
        }
        roles.add(2L);
        modulos.add(2L);
        submodulos.add(2L);

        userService.saveUser(user, roles, modulos, submodulos);
        return respuesta;
    }

}
