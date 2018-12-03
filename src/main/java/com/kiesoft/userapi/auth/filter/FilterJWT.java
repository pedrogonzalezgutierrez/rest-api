package com.kiesoft.userapi.auth.filter;

import com.kiesoft.userapi.auth.StatelessAuthentication;
import com.kiesoft.userapi.service.jwt.JwtService;
import com.kiesoft.userapi.service.stateless.StatelessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class FilterJWT extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final StatelessService statelessService;

    public FilterJWT(final AuthenticationManager authenticationManager, final JwtService jwtService, final StatelessService statelessService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.statelessService = statelessService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (Objects.nonNull(request.getHeader(HttpHeaders.AUTHORIZATION)) && request.getHeader(HttpHeaders.AUTHORIZATION).startsWith(BEARER_PREFIX)) {
            // There is a token in the Authorization header
            String token = StringUtils.substringAfterLast(request.getHeader(HttpHeaders.AUTHORIZATION), BEARER_PREFIX).trim();

            // Checking if is valid
            try {
                Optional<String> issuer = jwtService.getIssuer(token);
                if (issuer.isPresent()) {
                    // Try to authenticate
                    Authentication finalAuthentication = authenticationManager.authenticate(new StatelessAuthentication(issuer.get(), token));
                    if (Objects.nonNull(finalAuthentication)) {
                        statelessService.authenticate(finalAuthentication);
                    }
                }
            } catch (Exception e) {
                // It is not even a JWT token so dont do anything and continue with the flow so the user wont be authenticated
            }
        }

        // No token in the Authorization header, just keep going in the Filter Chain
        filterChain.doFilter(request, response);
    }

}