package com.kiesoft.userapi.service.jwt;

import java.util.Optional;
import java.util.UUID;

public interface JwtService {

    Boolean hasExpired(String jwt);

    Optional<String> generateHS256(UUID id, String secret);
    Boolean verifyHS256(String jwt, String secret);

    Optional<String> generateRS256(UUID id, String secret);
    Boolean verifyRS256(String jwt, String secret);

    Optional<String> getIssuer(String jwt);
    Optional<String> getIssueTime(String jwt);
    Optional<String> getExpirationTime(String jwt);

}
