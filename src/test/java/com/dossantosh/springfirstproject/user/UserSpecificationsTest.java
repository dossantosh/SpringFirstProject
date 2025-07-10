package com.dossantosh.springfirstproject.user;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.repository.UserRepository;
import com.dossantosh.springfirstproject.user.utils.UserSpecifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)  // Asegura que Spring y JUnit 5 trabajen juntos
@DataJpaTest
class UserSpecificationsTest {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("Should filter users by username")
    void filterByUsername() {
        User user1 = new User();
        user1.setUsername("alice");
        user1.setEmail("alice@domain.com");
        user1.setEnabled(true);
        user1.setPassword("pass");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("bob");
        user2.setEmail("bob@domain.com");
        user2.setEnabled(true);
        user2.setPassword("pass");
        userRepository.save(user2);

        Specification<User> spec = UserSpecifications.filterAdmin(null, "ali", null);
        List<User> result = userRepository.findAll(spec);

        assertThat(result).extracting(User::getUsername).contains("alice");
        assertThat(result).extracting(User::getUsername).doesNotContain("bob");
    }
}
