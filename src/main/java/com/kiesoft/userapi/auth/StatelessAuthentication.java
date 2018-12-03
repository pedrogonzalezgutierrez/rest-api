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
     * UUID of the user
     */
    private UUID id;

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
	 * Use this constructor when you want TO TRY to authenticate by token and id
	 */
	public StatelessAuthentication(String token, String issuer) {
		this.token = token;
		this.name = issuer;
		this.id = UUID.fromString(issuer);
		this.isAuthenticated = Boolean.FALSE;
	}

	/**
	 * Use this constructor for populating a User with Roles
	 */
	public StatelessAuthentication(String issuer, String password, String token, Collection<? extends GrantedAuthority> collection) {
		this.name = issuer;
        this.id = UUID.fromString(issuer);
		this.password = password;
		this.token = token;
		this.roles = collection;
		this.isAuthenticated = Boolean.TRUE;
	}

	/**
	 * username
	 */
	@Override
	public String getName() {
		return name;
	}

    public UUID getId() {
        return id;
    }

    /**
	 * Roles
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	/**
	 * The credentials that prove the principal is correct. This is usually a password, but could be anything relevant to the AuthenticationManager
	 */
	@Override
	public Object getCredentials() {
		return password;
	}

	/**
	 * Stores additional details about the authentication request. These might be an IP address, certificate serial number or any other metadata
	 */
	@Override
	public Object getDetails() {
		return token;
	}

	/**
	 * The identity of the principal being authenticated.
	 */
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
