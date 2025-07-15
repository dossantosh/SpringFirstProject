package com.dossantosh.springfirstproject.user.utils.projections;

public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private Boolean enabled;
    private Boolean isAdmin;

    public UserDTO(Long id, String username, String email, Boolean enabled, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
