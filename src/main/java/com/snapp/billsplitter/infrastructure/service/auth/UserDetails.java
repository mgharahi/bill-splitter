package com.snapp.billsplitter.infrastructure.service.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public final class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final String username;
    private final String password;
    @Getter
    private final Long id;

    public UserDetails(Long id, String username,String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}