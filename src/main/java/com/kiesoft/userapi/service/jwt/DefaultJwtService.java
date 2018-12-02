package com.kiesoft.userapi.service.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class DefaultJwtService implements JwtService {

    @Override
    public Optional<String> jwtHS256(UUID id, String secret) {
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
    public Optional<String> jwtRS256(UUID id, String secret) {
        // https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-rsa-signature
        return Optional.empty();
    }

}
