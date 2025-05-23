package com.example.springfirstproject.common.config.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CaptchaValidationFilter extends OncePerRequestFilter {

    private final ReCaptchaValidationService captchaService;
    
    private final AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler(
            "/login?captchaError=true");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        // Solo procesar la validación de captcha en la URL de login (POST)
        if ("/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String captchaToken = request.getParameter("g-recaptcha-response");
            boolean valid = captchaService.validateCaptcha(captchaToken);

            if (!valid) {
                // Si reCAPTCHA no es válido, finalizamos aquí con un fallo de autenticación
                failureHandler.onAuthenticationFailure(request, response,
                        new AuthenticationException("Captcha inválido") {
                        });
                return;
            }
        }

        // Si pasa la validación, continua con la cadena normal de filtros
        filterChain.doFilter(request, response);
    }
}