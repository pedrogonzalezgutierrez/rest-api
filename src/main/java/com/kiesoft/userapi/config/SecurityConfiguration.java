package com.kiesoft.userapi.config;

import com.kiesoft.userapi.auth.filter.AuthorizationFilterJWT;
import com.kiesoft.userapi.auth.provider.AuthenticationProviderJWT;
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

import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CREATE;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_JWT;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_UPDATE_PASSWORD;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StatelessService statelessService;

    // Secured Requests
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()

                // No session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                // No CSRF
                .csrf().disable()

                // Add Filter
                .addFilter(jwtAuthorizationFilter())
                .authorizeRequests()
                .antMatchers(HttpMethod.PATCH, ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD).authenticated();
    }

    // Spring Security will ignore these requests
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers(HttpMethod.POST, ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .antMatchers(HttpMethod.GET, ROUTING_USER_CONTROLLER + ROUTING_USER_JWT);
    }

    // JWT Authentication Provider
    @Bean
    public AuthenticationProviderJWT authenticationProviderJWT() throws Exception {
        return new AuthenticationProviderJWT(userService, jwtService);
    }

    // Set AuthenticationProviderJWT into AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderJWT());
    }

    //    @Bean
    public AuthorizationFilterJWT jwtAuthorizationFilter() throws Exception {
        AuthorizationFilterJWT authorizationFilterJWT = new AuthorizationFilterJWT(authenticationManager());
        authorizationFilterJWT.setJwtService(jwtService);
        authorizationFilterJWT.setStatelessService(statelessService);
        return authorizationFilterJWT;
    }

}