package com.kiesoft.userapi.service.stateless

import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.auth.StatelessAuthentication
import com.kiesoft.userapi.auth.authority.DefaultGrantedAuthority
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles(value = "integrationTest")
class DefaultStatelessServiceSpec extends Specification {

    final defaultStatelessService = new DefaultStatelessService()

    final uuid = UUID.randomUUID()
    final stringUUID = uuid.toString()
    final username = "pEDROLA"
    final email = "pedro@kiesoft.com"
    final password = "Betis"
    final roles = [new DefaultGrantedAuthority(TestDataService.ROLE_ADMIN), new DefaultGrantedAuthority(TestDataService.ROLE_EDITOR)]
    final jwt = "jwt"

    def "will authenticate a StatelessAuthentication object"() {
        given:
        final statelessAuthentication = new StatelessAuthentication(stringUUID, username, email, password, roles, jwt)

        when:
        defaultStatelessService.authenticate(statelessAuthentication)

        then:
        final actualAuthentication = defaultStatelessService.getAuthentication()

        and:
        Objects.nonNull(actualAuthentication)
        actualAuthentication.getId() == uuid
        actualAuthentication.getName() == stringUUID
        actualAuthentication.getUsername() == username
        actualAuthentication.getEmail() == email
        actualAuthentication.getCredentials() == password
        actualAuthentication.getDetails() == jwt
        actualAuthentication.getAuthorities() == roles
    }

}
