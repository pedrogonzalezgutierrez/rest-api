package com.kiesoft.userapi.auth

import com.kiesoft.userapi.auth.authority.DefaultGrantedAuthority
import spock.lang.Specification

class StatelessAuthenticationSpec extends Specification{

    final uuid = UUID.randomUUID()
    final stringUUID = uuid.toString()
    final username = "pEDROLA"
    final email = "pedro@kiesoft.com"
    final password = "Betis"
    final jwt = "jwt"
    final roles = [new DefaultGrantedAuthority("ROLE_ADMIN"), new DefaultGrantedAuthority("ROLE_STUFF")]

    def "constructor issuer and token: populate a no authenticated object"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            token == jwt
            isAuthenticated == Boolean.FALSE
        }

        and:
        statelessAuthentication.getId() == uuid
    }

    def "constructor issuer and token: populate a no authenticated object and then authenticated it"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            token == jwt
            isAuthenticated == Boolean.FALSE
        }

        and:
        statelessAuthentication.getId() == uuid

        and:
        statelessAuthentication.setAuthenticated(Boolean.TRUE)

        and:
        statelessAuthentication.isAuthenticated()
    }

    def "constructor issuer, password, token and roles: populate an authenticated object"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, username, email, password, roles, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            password == password
            roles == roles
            token == jwt
            isAuthenticated == Boolean.TRUE
        }

        and:
        statelessAuthentication.getId() == uuid

        and:
        statelessAuthentication.getUsername() == username
        statelessAuthentication.getEmail() == email
    }

    def "constructor issuer, password, token and roles: populate an authenticated object and then deauthenticate it"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, username, email, password, roles, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            password == password
            roles == roles
            token == jwt
            isAuthenticated == Boolean.TRUE
        }

        and:
        statelessAuthentication.getId() == uuid

        and:
        statelessAuthentication.getUsername() == username
        statelessAuthentication.getEmail() == email

        and:
        statelessAuthentication.setAuthenticated(Boolean.FALSE)

        and:
        !statelessAuthentication.isAuthenticated()
    }

}
