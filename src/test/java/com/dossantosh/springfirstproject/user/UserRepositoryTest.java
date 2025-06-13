package com.dossantosh.springfirstproject.user;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find user by username")
    void findByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@domain.com");
        user.setEnabled(true);
        user.setPassword("password");
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@domain.com");
    }

    @Test
    @DisplayName("Should check existence by email")
    void existsByEmail() {
        User user = new User();
        user.setUsername("anotheruser");
        user.setEmail("another@domain.com");
        user.setEnabled(true);
        user.setPassword("password");
        userRepository.save(user);

        assertThat(userRepository.existsByEmail("another@domain.com")).isTrue();
        assertThat(userRepository.existsByEmail("notfound@domain.com")).isFalse();
    }
}
