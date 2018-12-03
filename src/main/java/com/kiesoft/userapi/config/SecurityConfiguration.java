package com.kiesoft.userapi.config;

import com.kiesoft.userapi.auth.filter.TokenFilterJWT;
import com.kiesoft.userapi.auth.provider.AuthenticationProviderJWT;
import com.kiesoft.userapi.service.jwt.JwtService;
import com.kiesoft.userapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CREATE;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_JWT;
import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    /*
     * Requests secured
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // No session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // No CSRF
        http.csrf().disable();

        http.addFilterBefore(tokenJWTFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/role/**").authenticated();
    }

    /*
     * All of Spring Security will ignore these requests
     */
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .antMatchers(HttpMethod.POST, ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .antMatchers(HttpMethod.GET, ROUTING_USER_CONTROLLER + ROUTING_USER_JWT);
    }

    // Set AuthenticationProviderJWT into AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderJWT());
    }

    // JWT Token Filter
    @Bean
    public TokenFilterJWT tokenJWTFilter() throws Exception {
        TokenFilterJWT tokenJWTFilter = new TokenFilterJWT(authenticationManager(), jwtService);
        return tokenJWTFilter;
    }

    // JWT Authentication Provider
    @Bean
    public AuthenticationProviderJWT authenticationProviderJWT() throws Exception {
        return new AuthenticationProviderJWT(userService, jwtService);
    }

}