package com.dossantosh.springfirstproject.user.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dossantosh.springfirstproject.common.config.security.ReCaptchaValidationService;
import com.dossantosh.springfirstproject.objects.token.model.Token;
import com.dossantosh.springfirstproject.objects.token.service.TokenService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;

    private final TokenService tokenService;

    private final ReCaptchaValidationService reCaptchaValidationService;

    private final PasswordEncoder passwordEncoder;

    private static final String FAILURE_COUNT = "LOGIN_FAILURE_COUNT";

    private static final String LOGIN = "login";

    private static final String FORGOTPASSWORDEMAIL = "forgotPasswordEmail";

    private static final String FORGOTPASSWORD = "forgotPassword";

    private static final String ERROR = "error";

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean showRecover = false;

        if (session != null) {
            Integer failureCount = (Integer) session.getAttribute(FAILURE_COUNT);
            if (failureCount != null && failureCount >= 2) {
                showRecover = true;
            }
        }

        model.addAttribute("showRecoverPassword", showRecover);
        return LOGIN;
    }

    @GetMapping("/forgotPasswordEmail")
    public String showPasswordEmail(Model model) {
        return FORGOTPASSWORDEMAIL;
    }

    @PostMapping("/forgotPasswordEmail")
    @Transactional
    public String sendEmail(
            Model model,
            @RequestParam("g-recaptcha-response") String recaptchaToken,
            @RequestParam String email) {

        // Verificar el token de reCAPTCHA
        if (!reCaptchaValidationService.validateCaptcha(recaptchaToken)) {
            model.addAttribute(ERROR, "La verificación de reCAPTCHA falló. Por favor, intenta de nuevo.");
            return FORGOTPASSWORDEMAIL;
        }

        if (!userService.existsByEmail(email)) {
            return FORGOTPASSWORDEMAIL;
        }

        if (tokenService.existsByUser(userService.findByEmail(email))) {
            return LOGIN;
        }

        String token = tokenService.createVerificationToken(userService.findByEmail(email));
        tokenService.sendVerificationEmailPassword(email, token);

        return LOGIN;
    }

    @GetMapping("/forgotPassword")
    @Transactional
    public String confirmPasswordEmail(Model model,
            @RequestParam("token") String getToken) {
        Optional<Token> vToken = tokenService.findByToken(getToken);

        if (!vToken.isPresent()) {
            return "redirect:/token-invalid";
        }

        Token token = vToken.get();

        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "redirect:/token-invalid";
        }

        model.addAttribute("userId", token.getUser().getId());

        tokenService.deleteByToken(token.getToken());
        return FORGOTPASSWORD;
    }

    @PostMapping("/forgotPassword")
    @Transactional
    public String confirmPEmail(Model model,
            @RequestParam("g-recaptcha-response") String recaptchaToken,
            @RequestParam Long userId,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword) {

        // Verificar el token de reCAPTCHA
        if (!reCaptchaValidationService.validateCaptcha(recaptchaToken)) {
            model.addAttribute(ERROR, "La verificación de reCAPTCHA falló. Por favor, intenta de nuevo.");
            return FORGOTPASSWORD;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute(ERROR, "Las contraseñas no coinciden.");
            return FORGOTPASSWORD;
        }

        User user = userService.findById(userId);
        user.setPassword(newPassword);
        if (!user.getPassword().startsWith("{bcrypt}")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.saveUser(user);
        return LOGIN;
    }
}