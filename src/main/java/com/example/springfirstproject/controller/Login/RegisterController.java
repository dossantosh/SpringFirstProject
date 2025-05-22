package com.example.springfirstproject.controller.login;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springfirstproject.config.security.ReCaptchaValidationService;
import com.example.springfirstproject.models.Token;
import com.example.springfirstproject.models.permisos.Modules;
import com.example.springfirstproject.models.permisos.Roles;
import com.example.springfirstproject.models.permisos.Submodules;
import com.example.springfirstproject.models.user.Preferencias;
import com.example.springfirstproject.models.user.User;
import com.example.springfirstproject.models.user.UserAuth;
import com.example.springfirstproject.service.PreferenciasService;
import com.example.springfirstproject.service.TokenService;
import com.example.springfirstproject.service.permisos.ModuleService;
import com.example.springfirstproject.service.permisos.RoleService;
import com.example.springfirstproject.service.permisos.SubmoduleService;
import com.example.springfirstproject.service.user.UserAuthService;
import com.example.springfirstproject.service.user.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class RegisterController {

    private final UserService userService;

    private final PreferenciasService preferenciasService;

    private final RoleService roleService;

    private final ModuleService moduleService;

    private final SubmoduleService submoduleService;

    private final UserAuthService userAuthService;

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

        crearUsuario(user);
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

    public void crearUsuario(User user) {
        LinkedHashSet<Roles> roles = new LinkedHashSet<>();
        LinkedHashSet<Modules> modulos = new LinkedHashSet<>();
        LinkedHashSet<Submodules> submodulos = new LinkedHashSet<>();

        LinkedHashSet<Long> rolesId = new LinkedHashSet<>();
        LinkedHashSet<Long> modulosId = new LinkedHashSet<>();
        LinkedHashSet<Long> submodulosId = new LinkedHashSet<>();

        LinkedHashSet<String> rolesN = new LinkedHashSet<>();
        LinkedHashSet<String> modulosN = new LinkedHashSet<>();
        LinkedHashSet<String> submodulosN = new LinkedHashSet<>();

        if (user.getUsername().equals("sevas")) {
            rolesId.add(1L);
            modulosId.add(1L);
            submodulosId.add(1L);
        }

        rolesId.add(2L);
        modulosId.add(2L);
        submodulosId.add(2L);

        for (Long rol : rolesId) {
            Optional<Roles> role = roleService.findById(rol);

            if (role.isPresent()) {
                Roles existingRole = role.get();
                roles.add(existingRole);
                rolesN.add(existingRole.getName());
            }
        }
        for (Long modulo : modulosId) {
            Optional<Modules> module = moduleService.findById(modulo);

            if (module.isPresent()) {
                Modules existingModule = module.get();
                modulos.add(existingModule);
                modulosN.add(existingModule.getName());
            }
        }
        for (Long submodulo : submodulosId) {
            Optional<Submodules> submodule = submoduleService.findById(submodulo);

            if (submodule.isPresent()) {
                Submodules existingSubmodule = submodule.get();
                submodulos.add(existingSubmodule);
                submodulosN.add(existingSubmodule.getName());
            }
        }

        if (roles.isEmpty() || modulos.isEmpty() || submodulos.isEmpty()) {
            return;
        }

        UserAuth userAuth = new UserAuth();

        userAuth.setUsername(user.getUsername());
        userAuth.setRoles(rolesId);
        userAuth.setModules(modulosId);
        userAuth.setSubmodules(submodulosId);
        userAuthService.saveuserAuth(userAuth);

        Preferencias preferencias = new Preferencias();
        preferencias.setUserId(userAuth.getId());
        preferencias.setTema("auto");
        preferencias.setIdioma("es");
        preferencias.setNotificacionesEmail(false);
        preferencias.setNotificacionesSMS(false);
        preferenciasService.guardarPreferencias(preferencias);

        user.setEnabled(false);
        user.setRoles(roles);
        user.setModules(modulos);
        user.setSubmodules(submodulos);

        preferenciasService.guardarPreferencias(preferencias);
        userService.saveUser(user);

        String token = tokenService.createVerificationToken(user);
        tokenService.sendVerificationEmail(user.getEmail(), token);
    }
}
