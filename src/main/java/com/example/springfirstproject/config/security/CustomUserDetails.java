package com.example.springfirstproject.config.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springfirstproject.models.User.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    
    private static User user;

    /**
     * Devuelve la lista de GrantedAuthority a partir de los roles de User.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                   .stream()
                   .map(role -> new SimpleGrantedAuthority(role.getName()))
                   .collect(Collectors.toList());
    }

    /**
     * Devuelve la contraseña cifrada del usuario.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Devuelve el nombre de usuario único.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Indica si la cuenta no ha expirado.
     * Por defecto devolvemos true; cambialo si tienes lógica de expiración.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta no está bloqueada.
     * Por defecto devolvemos true; cambialo si tienes campo 'locked' en User.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales (contraseña) no han expirado.
     * Por defecto devolvemos true; cambialo si tienes lógica de expiración de credenciales.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta está habilitada.
     */
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}