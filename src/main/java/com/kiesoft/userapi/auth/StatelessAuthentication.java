package com.kiesoft.userapi.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class StatelessAuthentication implements Authentication {

    /**
     * This represents the String UUID of the user
     */
    private String name;

    /**
     * Email of the user
     */
    private String email;

    /**
     * Username
     */
    private String username;

    /**
     * User password
     */
    private String password;

    /**
     * User roles
     */
    private Collection<? extends GrantedAuthority> roles = new ArrayList<>();

    /**
     * Is authenticated?
     */
    private Boolean isAuthenticated;

    /**
     * User token for this request
     */
    private String token;

    /**
     * Use this constructor when you want TO TRY to authenticate by Issuer (id/uuid) and token
     */
    public StatelessAuthentication(String issuer, String token) {
        this.name = issuer;
        this.token = token;
        this.isAuthenticated = Boolean.FALSE;
    }

    /**
     * Use this constructor for populating a User with Roles
     */
    public StatelessAuthentication(String issuer, String name, String email, String password, Collection<? extends GrantedAuthority> collection, String token) {
        this.name = issuer;
        this.username = name;
        this.email = email;
        this.password = password;
        this.roles = collection;
        this.token = token;
        this.isAuthenticated = Boolean.TRUE;
    }

    @Override
    public String getName() {
        return name;
    }

    public UUID getId() {
        return UUID.fromString(name);
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return name;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /**
     * If you want to deauthenticate the user on the fly
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

}
