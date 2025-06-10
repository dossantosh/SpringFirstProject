package com.dossantosh.springfirstproject.user.models;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dossantosh.springfirstproject.user.models.objects.Preferences;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth implements UserDetails {

    private Long id;
    
    private String username;

    private String password;

    private Boolean enabled;

    private Preferences preferences;

    private Set<Long> roles = new LinkedHashSet<>();
    private Set<Long> modules = new LinkedHashSet<>();
    private Set<Long> submodules = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(roleId -> (GrantedAuthority) () -> "ROLE_" + roleId)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}
