package com.kiesoft.userapi.service.jwt

import spock.lang.Ignore
import spock.lang.Specification

class DefaultJwtServiceSpec extends Specification {

    final defaultJwtService = new DefaultJwtService()

    def "will reject JWT token is has expired"() {
        given:
        final jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIyZmIyODEzMS1lODQxLTRjMmQtOWZlNi1iYzVkZGI3MDg3MDQiLCJleHAiOjE1NDM5NzE0ODIsImlhdCI6MTU0Mzk2Nzg4Mn0.DhD0UK9dlYhoz9XZ5Jp0Nv457tm47yr65LD-CVbvAYg"

        when:
        final actual = defaultJwtService.hasExpired(jwt)

        then:
        actual
    }

    def "will generate HS256 JWT token"() {
        given:
        final id = UUID.randomUUID()
        final secret = "3a2c1fa3b5a6203a226c9384341f1222"

        when:
        final actual = defaultJwtService.generateHS256(id, secret)

        then:
        actual.isPresent()

        and: "verification successful"
        defaultJwtService.verifyHS256(actual.get(), secret)

        and: "payload contains iss (Issuer), iat (Issue Time) and exp (Expiration Time)"
        defaultJwtService.getIssuer(actual.get()).isPresent()
        defaultJwtService.getIssueTime(actual.get()).isPresent()
        defaultJwtService.getExpirationTime(actual.get()).isPresent()
    }

    def "will not generate HS256 JWT token when the secret length is not at least 256 bits"() {
        given:
        final id = UUID.randomUUID()
        final password = "betis"

        when:
        final actual = defaultJwtService.generateHS256(id, password)

        then:
        !actual.isPresent()
    }

    @Ignore
    def "will generate RS256 JWT token"() {
        given:
        final id = UUID.randomUUID()
        final secret = "3a2c1fa3b5a6203a226c9384341f1222"

        when:
        final actual = defaultJwtService.generateRS256(id, secret)

        then:
        actual.isPresent()
    }

    def "will not generate RS256 JWT token when the secret length is not at least 256 bits"() {
        given:
        final id = UUID.randomUUID()
        final password = "betis"

        when:
        final actual = defaultJwtService.generateRS256(id, password)

        then:
        !actual.isPresent()
    }

}
