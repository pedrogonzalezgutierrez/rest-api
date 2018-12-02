package com.kiesoft.userapi.service.jwt;

import java.util.Optional;
import java.util.UUID;

public interface JwtService {

    Optional<String> jwtHS256(UUID id, String secret);
    Optional<String> jwtRS256(UUID id, String secret);

}
