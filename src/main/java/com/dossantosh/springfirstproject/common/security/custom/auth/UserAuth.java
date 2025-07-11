package com.dossantosh.springfirstproject.common.security.custom.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dossantosh.springfirstproject.pref.models.Preferences;

import java.util.Collection;
import java.util.LinkedHashSet;
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

    private LinkedHashSet<String> roles = new LinkedHashSet<>();

    private LinkedHashSet<Long> modules = new LinkedHashSet<>();

    private LinkedHashSet<Long> submodules = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("" + role))
                .collect(Collectors.toList());
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
