package com.kiesoft.userapi.jpa.repository.user


import com.kiesoft.userapi.jpa.entity.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class UserRepositorySpec extends Specification {

    @Autowired
    private UserRepository userRepository

    final userAdmin = new UserEntity.Builder()
            .name("TheAdmin")
            .email("TheAdmin@kiesoft.es")
            .password("Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def "save an UserEntity without id (UUID) and without Roles"() {
        given:
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findAll()

        then: "there is only one user"
        actual.size() == 1

        and: "fields are saved successfully"
        with(actual.getAt(0)) {
            id != null
            name == userAdmin.name
            email == userAdmin.email
            password == userAdmin.password
            enabled == userAdmin.enabled
            points == userAdmin.points
        }

        and: "there is no roles"
        actual.getAt(0).getRoles().isEmpty()
    }

    def "find by name"() {
        given:
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findByNameIgnoreCase(name)

        then:
        actual.isPresent()
        with(actual.get()) {
            id != null
            name == userAdmin.name
            email == userAdmin.email
            password == userAdmin.password
            enabled == userAdmin.enabled
            points == userAdmin.points
        }
        where:
        name                     || _
        "TheAdmin".toUpperCase() || __
        "TheAdmin".toLowerCase() || __
    }

    def "find by email"() {
        given:
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findByEmailIgnoreCase(email)

        then:
        actual.isPresent()
        with(actual.get()) {
            id != null
            name == userAdmin.name
            email == userAdmin.email
            password == userAdmin.password
            enabled == userAdmin.enabled
            points == userAdmin.points
        }
        where:
        email                             || _
        "pEDROLA@correo.es".toUpperCase() || __
        "pEDROLA@correo.es".toLowerCase() || __

    }

}
