package com.kiesoft.userapi.auth.filter;

import com.kiesoft.userapi.auth.StatelessAuthentication;
import com.kiesoft.userapi.service.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class TokenFilterJWT extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenFilterJWT(final AuthenticationManager authenticationManager, final JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Get the token from the request
        String token = request.getParameter("token");

        if (Objects.nonNull(token)) {
            // There is a token in the request, checking if is valid
            try {
                Optional<String> issuer = jwtService.getIssuer(token);
                if (issuer.isPresent()) {
                    // Try to authenticate
                    Authentication finalAuthentication = authenticationManager.authenticate(new StatelessAuthentication(token, issuer.get()));
                    if (Objects.nonNull(finalAuthentication)) {
                        SecurityContextHolder.getContext().setAuthentication(finalAuthentication);
                    }
                }
            } catch (Exception e) {
                // Dont do anything, continue with the flow so the user wont be authenticated
            }
        }

        // Keep going in the Filter Chain
        filterChain.doFilter(request, response);
    }

}