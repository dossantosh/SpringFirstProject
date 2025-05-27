package com.dossantosh.springfirstproject.user.controller;

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

import com.dossantosh.springfirstproject.common.config.security.ReCaptchaValidationService;
import com.dossantosh.springfirstproject.objects.token.model.Token;
import com.dossantosh.springfirstproject.objects.token.service.TokenService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.service.UserService;

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
    
    private static final String REGISTER = "register";

    private static final String ERROR = "error";

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return REGISTER;
    }

    @PostMapping("/register")
    @Transactional
    public String registerUser(@Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model,
            @RequestParam("g-recaptcha-response") String recaptchaToken) {
        String respuesta = "redirect:/login?success=Registro+exitoso.+Por+favor+inicia+sesión";

        if (result.hasErrors()) {
            return REGISTER;
        }

        // Verificar el token de reCAPTCHA
        if (!reCaptchaValidationService.validateCaptcha(recaptchaToken)) {
            model.addAttribute(ERROR, "La verificación de reCAPTCHA falló. Por favor, intenta de nuevo.");
            return REGISTER;
        }

        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute(ERROR, "El nombre de usuario ya existe");
            return REGISTER;
        }

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute(ERROR, "El correo ya existe");
            return REGISTER;
        }

        if (!user.getPassword().startsWith("{bcrypt}")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.crearUsuario(user);
        return respuesta;
    }

    @GetMapping("/confirm/user")
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