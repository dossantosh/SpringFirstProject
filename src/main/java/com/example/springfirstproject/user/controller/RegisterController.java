package com.example.springfirstproject.user.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springfirstproject.common.config.security.ReCaptchaValidationService;
import com.example.springfirstproject.objects.token.model.Token;
import com.example.springfirstproject.objects.token.service.TokenService;
import com.example.springfirstproject.user.models.User;
import com.example.springfirstproject.user.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class RegisterController {

    private final UserService userService;

    private final TokenService tokenService;

    private final ReCaptchaValidationService reCaptchaValidationService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Asegúrate que este es el nombre exacto de tu plantilla
    }

    @PostMapping("/register")
    @Transactional
    public String registerUser(@Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model,
            @RequestParam("g-recaptcha-response") String recaptchaToken) {
        String respuesta = "redirect:/login?success=Registro+exitoso.+Por+favor+inicia+sesión";

        if (result.hasErrors()) {
            return "register";
        }

        // Verificar el token de reCAPTCHA
        if (!reCaptchaValidationService.validateCaptcha(recaptchaToken)) {
            model.addAttribute("error", "La verificación de reCAPTCHA falló. Por favor, intenta de nuevo.");
            return "register";
        }

        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "register";
        }

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "El correo ya existe");
            return "register";
        }

        if (!user.getPassword().startsWith("{bcrypt}")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.crearUsuario(user);
        return respuesta;
    }

    @GetMapping("/confirm")
    @Transactional
    public String confirmRegistration(@RequestParam("token") String getToken) {
        Optional<Token> vToken = tokenService.findByToken(getToken);

        if (!vToken.isPresent()) {
            return "redirect:/token-invalid";
        }

        Token token = vToken.get();

        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "redirect:/token-invalid";
        }

        User user = token.getUser();
        user.setEnabled(true);
        userService.saveUser(user);
        tokenService.deleteByToken(token.getToken());
        return "redirect:/confirmacion-exitosa";
    }
}