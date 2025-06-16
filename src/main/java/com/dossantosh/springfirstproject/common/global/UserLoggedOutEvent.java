package com.dossantosh.springfirstproject.common.global;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserLoggedOutEvent {

    private final Long userId;
    private final String username;

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}