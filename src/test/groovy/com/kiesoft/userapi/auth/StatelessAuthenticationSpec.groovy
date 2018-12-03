package com.kiesoft.userapi.auth

import com.kiesoft.userapi.auth.authority.DefaultGrantedAuthority
import spock.lang.Specification

class StatelessAuthenticationSpec extends Specification{

    final uuid = UUID.randomUUID()
    final stringUUID = uuid.toString()
    final password = "Betis"
    final jwt = "jwt"
    final roles = [new DefaultGrantedAuthority("ROLE_ADMIN"), new DefaultGrantedAuthority("ROLE_STUFF")]

    def "constructor issuer and token: populate a no authenticated object"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            id == uuid
            token == jwt
            isAuthenticated == Boolean.FALSE
        }
    }

    def "constructor issuer and token: populate a no authenticated object and then authenticated it"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            id == uuid
            token == jwt
            isAuthenticated == Boolean.FALSE
        }

        and:
        statelessAuthentication.setAuthenticated(Boolean.TRUE)

        and:
        statelessAuthentication.isAuthenticated()
    }

    def "constructor issuer, password, token and roles: populate an authenticated object"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, password, roles, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            id == uuid
            password == password
            roles == roles
            token == jwt
            isAuthenticated == Boolean.TRUE
        }
    }

    def "constructor issuer, password, token and roles: populate an authenticated object and then deauthenticate it"() {
        when:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, password, roles, jwt)

        then:
        with(statelessAuthentication) {
            name == stringUUID
            id == uuid
            password == password
            roles == roles
            token == jwt
            isAuthenticated == Boolean.TRUE
        }

        and:
        statelessAuthentication.setAuthenticated(Boolean.FALSE)

        and:
        !statelessAuthentication.isAuthenticated()
    }

}
