package com.kiesoft.userapi.service.jwt

import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jose.crypto.RSADecrypter
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import spock.lang.Ignore
import spock.lang.Specification

class DefaultJwtServiceSpec extends Specification {

    final defaultJwtService = new DefaultJwtService()

    def "will generate HS256 JWT token"() {
        given:
        final id = UUID.randomUUID()
        final secret = "3a2c1fa3b5a6203a226c9384341f1222"

        when:
        final actual = defaultJwtService.jwtHS256(id, secret)

        then:
        actual.isPresent()

        and:
        final jwsObject = JWSObject.parse(actual.get());
        final verifier = new MACVerifier(secret);

        and: "verity the token"
        jwsObject.verify(verifier)

        and: "payload contains iss (issuer), iat (issueTime) and exp (expirationTime"
        Objects.nonNull(jwsObject.getPayload().toJSONObject().get("iss"))
        Objects.nonNull(jwsObject.getPayload().toJSONObject().get("iat"))
        Objects.nonNull(jwsObject.getPayload().toJSONObject().get("exp"))
    }

    def "will not generate HS256 JWT token when the secret length is not at least 256 bits"() {
        given:
        final id = UUID.randomUUID()
        final password = "betis"

        when:
        final actual = defaultJwtService.jwtHS256(id, password)

        then:
        !actual.isPresent()
    }

    @Ignore
    def "will generate RS256 JWT token"() {
        given:
        final id = UUID.randomUUID()
        final secret = "3a2c1fa3b5a6203a226c9384341f1222"

        when:
        final actual = defaultJwtService.jwtRS256(id, secret)

        then:
        actual.isPresent()
    }

    def "will not generate RS256 JWT token when the secret length is not at least 256 bits"() {
        given:
        final id = UUID.randomUUID()
        final password = "betis"

        when:
        final actual = defaultJwtService.jwtRS256(id, password)

        then:
        !actual.isPresent()
    }

}
