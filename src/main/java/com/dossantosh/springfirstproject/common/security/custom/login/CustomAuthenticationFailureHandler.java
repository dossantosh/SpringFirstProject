package com.dossantosh.springfirstproject.common.security.custom.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String FAILURE_COUNT = "LOGIN_FAILURE_COUNT";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        HttpSession session = request.getSession(true);
        Integer failureCount = (Integer) session.getAttribute(FAILURE_COUNT);
        if (failureCount == null) {
            failureCount = 0;
        }
        failureCount++;
        session.setAttribute(FAILURE_COUNT, failureCount);

        // Redirigir a /login?error=true
        super.setDefaultFailureUrl("/login?error=true");
        super.onAuthenticationFailure(request, response, exception);
    }
}