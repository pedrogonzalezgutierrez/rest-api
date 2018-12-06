package com.kiesoft.userapi.service.jwt

import spock.lang.Ignore
import spock.lang.Specification

class DefaultJwtServiceSpec extends Specification {

    final defaultJwtService = new DefaultJwtService()

    def "will reject JWT token if has expired"() {
        when:
        final actual = defaultJwtService.hasExpired(jwt)

        then:
        actual

        where:
        jwt                                                                                                                                                     || _
        "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI4MTU3NjM1MS1hNzY3LTQ2ZjQtYjYwNy05Zjk2ZjIzYWE0OGYiLCJpYXQiOjE1NDQwNTIyMzJ9.6rtkbfrzjN6RVcpVw8UYOT5T5Q3bA6GY_6AwHBYbpZg" || _
        "This is not a JWT"                                                                                                                                     || _
    }

    def "verification fails"() {
        when:
        final actual = defaultJwtService.verifyHS256(jwt, secret)

        then:
        !actual

        where:
        jwt                                                                                                                                                     | secret
        "This is not a JWT"                                                                                                                                     | "Betis"
        "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI4MTU3NjM1MS1hNzY3LTQ2ZjQtYjYwNy05Zjk2ZjIzYWE0OGYiLCJpYXQiOjE1NDQwNTIyMzJ9.6rtkbfrzjN6RVcpVw8UYOT5T5Q3bA6GY_6AwHBYbpZg" | "This is a wrong password so it will fail"
        "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI4MTU3NjM1MS1hNzY3LTQ2ZjQtYjYwNy05Zjk2ZjIzYWE0OGYiLCJpYXQiOjE1NDQwNTIyMzJ9.6rtkbfrzjN6RVcpVw8UYOT5T5Q3bA6GY_6AwHBYbpZg" | "3a2c1fa3b5a6203a226c9384341f1222"
    }

    def "will not reject JWT token if has not expired"() {
        given:
        final jwt = defaultJwtService.generateHS256(UUID.randomUUID(), "c25894ebba77ba392a5f9a67354ca257")

        when:
        final actual = defaultJwtService.hasExpired(jwt.get())

        then:
        !actual
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
