package com.dossantosh.springfirstproject.user.integration;

import com.dossantosh.springfirstproject.user.models.User;

import com.dossantosh.springfirstproject.user.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Should register and retrieve user")
    void registerAndRetrieveUser() {
        User user = new User();
        user.setUsername("integration");
        user.setEmail("integration@domain.com");

        user.setEnabled(true);
        user.setPassword("password");
        userService.saveUser(user);

        User found = userService.findByUsername("integration");
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("integration@domain.com");
        assertThat(found.getEnabled()).isTrue();
    }
}