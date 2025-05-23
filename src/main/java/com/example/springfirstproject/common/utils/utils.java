package com.example.springfirstproject.common.utils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import com.example.springfirstproject.user.models.UserAuth;
import com.example.springfirstproject.user.service.UserAuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class utils {
    private final UserAuthService userAuthService;
    
        private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("username", auth.getName());
        if (auth.getName().equals("anonymousUser")) {
            return;
        }

        Optional<UserAuth> userAuth = userAuthService.findByUsername(auth.getName());
        if (!userAuth.isPresent()) {
            return;
        }
        model.addAttribute("userAuth", userAuth.get());
    }
}
