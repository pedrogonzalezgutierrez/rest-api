package com.kiesoft.userapi.service.stateless

import com.kiesoft.userapi.auth.StatelessAuthentication
import com.kiesoft.userapi.auth.authority.DefaultGrantedAuthority
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class DefaultStatelessServiceSpec extends Specification {

    final defaultStatelessService = new DefaultStatelessService()

    final uuid = UUID.randomUUID()
    final stringUUID = uuid.toString()
    final password = "Betis"
    final roles = [new DefaultGrantedAuthority("ROLE_ADMIN"), new DefaultGrantedAuthority("ROLE_STUFF")]
    final jwt = "jwt"

    def "will authenticate a StatelessAuthentication object"() {
        given:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, password, roles, jwt)

        when:
        defaultStatelessService.authenticate(statelessAuthentication)

        then:
        final actualAuthentication = defaultStatelessService.getAuthentication()

        and:
        Objects.nonNull(actualAuthentication)
        actualAuthentication.getId() == uuid
        actualAuthentication.getName() == stringUUID
        actualAuthentication.getCredentials() == password
        actualAuthentication.getDetails() == jwt
        actualAuthentication.getAuthorities() == roles
    }

}