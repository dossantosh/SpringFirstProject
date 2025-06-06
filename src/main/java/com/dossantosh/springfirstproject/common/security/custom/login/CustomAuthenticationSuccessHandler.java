package com.dossantosh.springfirstproject.common.security.custom.login;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.UserAuth;
import com.dossantosh.springfirstproject.user.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();

        User user;
        if(authentication != null){
            user = userService.findByUsername(authentication.getName());
        } else{
            UserAuth userAuth = (UserAuth) session.getAttribute("userAuth");
            user = userService.findById(userAuth.getId());
        }

        session.setAttribute("userAuth", userService.userToUserAuth(user));

        if(authentication != null){
            response.sendRedirect("/objects/news");
        }
    }
}