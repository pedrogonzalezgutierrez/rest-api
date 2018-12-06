package com.kiesoft.userapi.service.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Math.toIntExact;

@Component
public class DefaultJwtService implements JwtService {

    private final static String ISSUER = "iss";
    private final static String ISSUE_TIME = "iat";
    private final static int EXPIRATION_MINUTES = 60;

    @Override
    public Boolean hasExpired(String jwt) {
        Optional<String> issueTime = getIssueTime(jwt);
        if (issueTime.isPresent()) {

            Date dateIssueTime = new Date(Long.valueOf(issueTime.get()) * 1000L);
            LocalDateTime ldtIssueTime = LocalDateTime.ofInstant(dateIssueTime.toInstant(), ZoneId.systemDefault());

            LocalDateTime ldtNow = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());

            int minutes = toIntExact(Duration.between(ldtIssueTime, ldtNow).toMinutes());

            if (minutes <= EXPIRATION_MINUTES) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Optional<String> generateHS256(UUID id, String secret) {
        // https://connect2id.com/products/nimbus-jose-jwt/examples/jws-with-hmac
        try {
            Date now = new Date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.HOUR_OF_DAY, -1);
//            Date now= calendar.getTime();

            JWSSigner signer = new MACSigner(secret);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(id.toString())
                    .issueTime(now)
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return Optional.of(signedJWT.serialize());
        } catch (JOSEException e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean verifyHS256(String jwt, String secret) {
        if (hasExpired(jwt)) {
            return Boolean.FALSE;
        }
        try {
            final JWSObject jwsObject = JWSObject.parse(jwt);
            final MACVerifier verifier = new MACVerifier(secret);
            return jwsObject.verify(verifier);
        } catch (Exception e) {
            // It is not even a JWT Token
            return Boolean.FALSE;
        }
    }

    @Override
    public Optional<String> generateRS256(UUID id, String secret) {
        // https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-rsa-signature
        return Optional.empty();
    }

    @Override
    public Boolean verifyRS256(String jwt, String secret) {
        return Boolean.FALSE;
    }

    @Override
    public Optional<String> getIssuer(String jwt) {
        return getClaim(jwt, ISSUER);
    }

    @Override
    public Optional<String> getIssueTime(String jwt) {
        return getClaim(jwt, ISSUE_TIME);
    }

    private Optional<String> getClaim(String jwt, String name) {
        try {
            final JWSObject jwsObject = JWSObject.parse(jwt);
            if (Objects.nonNull(jwsObject.getPayload().toJSONObject().get(name))) {
                return Optional.of(jwsObject.getPayload().toJSONObject().get(name).toString());
            }
            // Claim not found
            return Optional.empty();
        } catch (Exception e) {
            // It is not even a JWT Token
            return Optional.empty();
        }
    }
}
