package com.example.springfirstproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import com.example.springfirstproject.models.User.UserChikito;
import com.example.springfirstproject.repositories.User.UserChikitoRepository;
import com.example.springfirstproject.service.User.UserChikitoService;

@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class) //igual que mocks.close();
class UserChikitoServiceTest {

    @Mock
    private UserChikitoRepository userChikitoRepository;

    @InjectMocks
    private UserChikitoService userChikitoService;

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
        when(userChikitoRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean resultado = userChikitoService.existsByUsername(username);

        // Assert
        assertTrue(resultado, "El servicio debería devolver true cuando el repositorio indica que existe");
        verify(userChikitoRepository, times(1)).existsByUsername(username);
    }

    @Test
    void cuandoNoExisteUsername_existsByUsernameDevuelveFalse() {
        // Arrange
        String username = "pepito";
        when(userChikitoRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean resultado = userChikitoService.existsByUsername(username);

        // Assert
        assertFalse(resultado, "El servicio debería devolver false cuando el repositorio indica que no existe");
        verify(userChikitoRepository, times(1)).existsByUsername(username);
    }

    @Test
    void findByUsername_conUsuarioExistente_devuelveUserChikito() {
        // Arrange
        String username = "maria";
        UserChikito usuario = new UserChikito();
        usuario.setId(1L);
        usuario.setUsername(username);
        when(userChikitoRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        // Act
        UserChikito encontrado = userChikitoService.findByUsername(username);

        // Assert
        assertNotNull(encontrado, "El servicio no debería devolver null cuando el repositorio encuentra el usuario");
        assertEquals(1L, encontrado.getId(), "El ID devuelto debe coincidir con el del usuario mockeado");
        assertEquals(username, encontrado.getUsername());
        verify(userChikitoRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_sinUsuario_lanzaUsernameNotFoundException() {
        // Arrange
        String username = "noexisto";
        when(userChikitoRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException ex = assertThrows(
            UsernameNotFoundException.class,
            () -> userChikitoService.findByUsername(username),
            "Se debe lanzar UsernameNotFoundException cuando el usuario no existe"
        );
        assertTrue(ex.getMessage().contains(username));
        verify(userChikitoRepository, times(1)).findByUsername(username);
    }

    @Test
    void findAll_devuelveConjuntoDeUsuarios() {
        // Arrange
        UserChikito u1 = new UserChikito();
        u1.setId(1L);
        u1.setUsername("alice");
        UserChikito u2 = new UserChikito();
        u2.setId(2L);
        u2.setUsername("bob");
        when(userChikitoRepository.findAll()).thenReturn(java.util.List.of(u1, u2));

        // Act
        Set<UserChikito> todos = userChikitoService.findAll();

        // Assert
        assertNotNull(todos, "El resultado no debería ser null");
        assertEquals(2, todos.size(), "Debe devolver los dos usuarios mockeados");
        assertTrue(todos.contains(u1));
        assertTrue(todos.contains(u2));
        verify(userChikitoRepository, times(1)).findAll();
    }
}
