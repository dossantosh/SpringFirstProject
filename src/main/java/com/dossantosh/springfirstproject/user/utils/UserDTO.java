package com.dossantosh.springfirstproject.user.utils;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO implements Serializable{

    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El nombre de usuario no debe superar 50 caracteres")
    private String username;

    @Email(message = "Email no válido")
    @Size(max = 50, message = "El email no debe superar 50 caracteres")
    private String email;

    private Boolean enabled;

    @Size(max = 100, message = "La contraseña no debe superar 100 caracteres")
    private String password;

    private Set<String> rolesIds = new LinkedHashSet<>();

    private Set<String> modulesIds = new LinkedHashSet<>();

    private Set<String> submodulesIds = new LinkedHashSet<>();
}
