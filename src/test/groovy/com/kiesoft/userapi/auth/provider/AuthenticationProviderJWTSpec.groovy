package com.kiesoft.userapi.auth.provider

import com.kiesoft.userapi.auth.StatelessAuthentication
import com.kiesoft.userapi.auth.authority.DefaultGrantedAuthority
import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.service.jwt.JwtService
import com.kiesoft.userapi.service.user.UserService
import spock.lang.Specification

class AuthenticationProviderJWTSpec extends Specification {

    final userService = Mock(UserService)
    final jwtService = Mock(JwtService)
    final authenticationProviderJWT = new AuthenticationProviderJWT(userService, jwtService)

    final uuid = UUID.randomUUID()
    final stringUUID = uuid.toString()
    final password = "Betis"
    final jwt = "jwt"
    final roles = [new DefaultGrantedAuthority("ROLE_ADMIN"), new DefaultGrantedAuthority("ROLE_STUFF")]

    def "return a populated Authenticate object"() {
        given:
        final authenticateObject = new StatelessAuthentication(stringUUID, jwt)

        and: "stringUUID is found in database"
        final userDTO = new UserDTO.Builder()
                .id(uuid)
                .name("pEDROLA")
                .email("pedro@email.com")
                .password(password)
                .enabled(Boolean.TRUE)
                .points(100)
                .build()
        userService.findById(uuid) >> Optional.of(userDTO)

        and: "token is signed with the database's password"
        jwtService.verifyHS256(jwt, password) >> Boolean.TRUE

        when:
        final actual = authenticationProviderJWT.authenticate(authenticateObject)

        then:
        Objects.nonNull(actual)
    }

    def "will not return a populated Authenticate object because Issuer not found in database"() {
        given:
        final authenticateObject = new StatelessAuthentication(stringUUID, jwt)

        and: "stringUUID not found in database"
        userService.findById(uuid) >> Optional.empty()

        when:
        final actual = authenticationProviderJWT.authenticate(authenticateObject)

        then:
        Objects.isNull(actual)
    }

    def "will not return a populated Authenticate object because wrong credentials"() {
        given:
        final authenticateObject = new StatelessAuthentication(stringUUID, jwt)

        and: "stringUUID is found in database"
        final userDTO = new UserDTO.Builder()
                .id(uuid)
                .name("pEDROLA")
                .email("pedro@email.com")
                .password(password)
                .enabled(Boolean.TRUE)
                .points(100)
                .build()
        userService.findById(uuid) >> Optional.of(userDTO)

        and: "token is not signed with the database's password"
        jwtService.verifyHS256(jwt, password) >> Boolean.FALSE

        when:
        final actual = authenticationProviderJWT.authenticate(authenticateObject)

        then:
        Objects.isNull(actual)
    }

}
