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
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class DefaultJwtService implements JwtService {

    public final static String ISSUER = "iss";
    public final static String ISSUE_TIME = "iat";
    public final static String EXPIRATION_TIME = "exp";

    @Override
    public Optional<String> generateHS256(UUID id, String secret) {
        // https://connect2id.com/products/nimbus-jose-jwt/examples/jws-with-hmac
        try {
            Date now = new Date();
            Date expirationDate = DateUtils.addHours(now, 1);

            JWSSigner signer = new MACSigner(secret);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(id.toString())
                    .issueTime(now)
                    .expirationTime(expirationDate)
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

    @Override
    public Optional<String> getExpirationTime(String jwt) {
        return getClaim(jwt, EXPIRATION_TIME);
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
