package com.kiesoft.userapi.auth.provider;

import com.kiesoft.userapi.auth.StatelessAuthentication;
import com.kiesoft.userapi.auth.authority.DefaultGrantedAuthority;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.jwt.JwtService;
import com.kiesoft.userapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthenticationProviderJWT implements AuthenticationProvider {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationProviderJWT(final UserService userService, final JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(StatelessAuthentication.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        StatelessAuthentication statelessAuthentication = (StatelessAuthentication) authentication;

        final Optional<UserDTO> userDTO = userService.findById(statelessAuthentication.getId());
        if (userDTO.isPresent() && userDTO.get().getEnabled()) {
            String token = (String) statelessAuthentication.getDetails();
            if (jwtService.verifyHS256(token, userDTO.get().getPassword())) {
                // Token is valid and it is not expired

                // Set up Roles
                List<DefaultGrantedAuthority> roles = userDTO.get().getRoles().stream()
                        .map(roleDTO -> new DefaultGrantedAuthority(roleDTO.getName()))
                        .collect(Collectors.toList());

                return new StatelessAuthentication(
                        userDTO.get().getId().toString(),
                        userDTO.get().getName(),
                        userDTO.get().getEmail(),
                        userDTO.get().getPassword(),
                        roles,
                        (String) statelessAuthentication.getDetails());
            }
        }
        return null;
    }
}