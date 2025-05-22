package com.example.springfirstproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import com.example.springfirstproject.models.user.UserAuth;
import com.example.springfirstproject.repositories.user.UserAuthRepository;
import com.example.springfirstproject.service.user.UserAuthService;

@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class) //igual que mocks.close();
class userAuthServiceTest {

    @Mock
    private UserAuthRepository userAuthRepository;

    @InjectMocks
    private UserAuthService userAuthService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close(); // Cierra los mocks para liberar recursos
    }

    @Test
    void cuandoExisteUsername_existsByUsernameDevuelveTrue() {
        // Arrange
        String username = "juan";
        when(userAuthRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean resultado = userAuthService.existsByUsername(username);

        // Assert
        assertTrue(resultado, "El servicio debería devolver true cuando el repositorio indica que existe");
        verify(userAuthRepository, times(1)).existsByUsername(username);
    }

    @Test
    void cuandoNoExisteUsername_existsByUsernameDevuelveFalse() {
        // Arrange
        String username = "pepito";
        when(userAuthRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean resultado = userAuthService.existsByUsername(username);

        // Assert
        assertFalse(resultado, "El servicio debería devolver false cuando el repositorio indica que no existe");
        verify(userAuthRepository, times(1)).existsByUsername(username);
    }

    @Test
    void findByUsername_conUsuarioExistente_devuelveuserAuth() {
        // Arrange
        String username = "maria";
        UserAuth usuario = new UserAuth();
        usuario.setId(1L);
        usuario.setUsername(username);
        when(userAuthRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        // Act
        UserAuth encontrado = userAuthService.findByUsername(username).get();

        // Assert
        assertNotNull(encontrado, "El servicio no debería devolver null cuando el repositorio encuentra el usuario");
        assertEquals(1L, encontrado.getId(), "El ID devuelto debe coincidir con el del usuario mockeado");
        assertEquals(username, encontrado.getUsername());
        verify(userAuthRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_sinUsuario_lanzaUsernameNotFoundException() {
        // Arrange
        String username = "noexisto";
        when(userAuthRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException ex = assertThrows(
            UsernameNotFoundException.class,
            () -> userAuthService.findByUsername(username),
            "Se debe lanzar UsernameNotFoundException cuando el usuario no existe"
        );
        assertTrue(ex.getMessage().contains(username));
        verify(userAuthRepository, times(1)).findByUsername(username);
    }

    @Test
    void findAll_devuelveConjuntoDeUsuarios() {
        // Arrange
        UserAuth u1 = new UserAuth();
        u1.setId(1L);
        u1.setUsername("alice");
        UserAuth u2 = new UserAuth();
        u2.setId(2L);
        u2.setUsername("bob");
        when(userAuthRepository.findAll()).thenReturn(java.util.List.of(u1, u2));

        // Act
        Set<UserAuth> todos = userAuthService.findAll();

        // Assert
        assertNotNull(todos, "El resultado no debería ser null");
        assertEquals(2, todos.size(), "Debe devolver los dos usuarios mockeados");
        assertTrue(todos.contains(u1));
        assertTrue(todos.contains(u2));
        verify(userAuthRepository, times(1)).findAll();
    }
}
