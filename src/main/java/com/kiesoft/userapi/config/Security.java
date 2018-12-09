package com.kiesoft.userapi.config;

import com.kiesoft.userapi.auth.filter.AuthorizationFilterJWT;
import com.kiesoft.userapi.auth.provider.AuthenticationProviderJWT;
import com.kiesoft.userapi.domain.role.RoleConstants;
import com.kiesoft.userapi.service.jwt.JwtService;
import com.kiesoft.userapi.service.stateless.StatelessService;
import com.kiesoft.userapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.kiesoft.userapi.controller.role.AbstractRoleController.ROUTING_ROLE_CONTROLLER;
import static com.kiesoft.userapi.controller.role.AbstractRoleController.ROUTING_ROLE_CREATE;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CREATE;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_ENABLE_USER;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_JWT;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_ROLE;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_UPDATE_PASSWORD;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StatelessService statelessService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Secured Requests

        http.sessionManagement()

                // No session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // No CSRF
                .csrf().disable()

                // Secure next request with the JWT filter
                .addFilter(authorizationFilterJWT())
                .authorizeRequests()

                // Enable or disable an User
                .antMatchers(HttpMethod.PATCH, ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER).hasRole(RoleConstants.ROLE_ADMIN)

                // Add a Role to a User
                .antMatchers(HttpMethod.POST, ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE).hasRole(RoleConstants.ROLE_ADMIN)

                // Remove a Role from a User
                .antMatchers(HttpMethod.DELETE, ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE).hasRole(RoleConstants.ROLE_ADMIN)

                // Create a Role
                .antMatchers(HttpMethod.POST, ROUTING_ROLE_CONTROLLER + ROUTING_ROLE_CREATE).hasRole(RoleConstants.ROLE_ADMIN);
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        // These requests are ignore by Spring Security

        webSecurity
                .ignoring()

                // Create user
                .antMatchers(HttpMethod.POST, ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)

                // Get a JWT
                .antMatchers(HttpMethod.POST, ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)

                // Update the password of a user
                .antMatchers(HttpMethod.PATCH, ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD);
    }

    @Bean
    public AuthenticationProviderJWT authenticationProviderJWT() throws Exception {
        // JWT Authentication Provider
        return new AuthenticationProviderJWT(userService, jwtService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set AuthenticationProviderJWT into AuthenticationManager
        auth.authenticationProvider(authenticationProviderJWT());
    }

    private AuthorizationFilterJWT authorizationFilterJWT() throws Exception {
        AuthorizationFilterJWT authorizationFilterJWT = new AuthorizationFilterJWT(authenticationManager());
        authorizationFilterJWT.setJwtService(jwtService);
        authorizationFilterJWT.setStatelessService(statelessService);
        return authorizationFilterJWT;
    }

}