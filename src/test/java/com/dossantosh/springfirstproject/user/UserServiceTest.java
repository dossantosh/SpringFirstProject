package com.dossantosh.springfirstproject.user;

import com.dossantosh.springfirstproject.pref.PreferencesService;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.repository.UserRepository;
import com.dossantosh.springfirstproject.user.service.UserService;
import com.dossantosh.springfirstproject.user.service.objects.TokenService;
import com.dossantosh.springfirstproject.user.service.permissions.ModuleService;
import com.dossantosh.springfirstproject.user.service.permissions.RoleService;
import com.dossantosh.springfirstproject.user.service.permissions.SubmoduleService;
import com.dossantosh.springfirstproject.user.utils.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private ModuleService moduleService;
    @Mock
    private SubmoduleService submoduleService;
    @Mock
    private PreferencesService preferencesService;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save and retrieve user")
    void saveAndFindUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("serviceuser");
        user.setEmail("service@domain.com");
        user.setEnabled(true);
        user.setPassword("password");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User saved = userService.saveUser(user);
        assertThat(saved.getUsername()).isEqualTo("serviceuser");

        User found = userService.findById(1L);
        assertThat(found.getEmail()).isEqualTo("service@domain.com");
    }

    @Test
    @DisplayName("Should convert User to UserDTO")
    void convertUserToDTO() {
        User user = new User();
        user.setId(2L);
        user.setUsername("dtoUser");
        user.setEmail("dto@domain.com");
        user.setEnabled(true);

        Set<Roles> roles = new LinkedHashSet<>();
        Roles role = new Roles();
        role.setId(1L);
        role.setName("ADMIN");
        roles.add(role);
        user.setRoles(roles);

        UserDTO dto = userService.convertirUsuariosADTO(Set.of(user)).iterator().next();
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getUsername()).isEqualTo("dtoUser");
    }
}
