package com.kiesoft.userapi.jpa.repository.user

import com.kiesoft.userapi.jpa.entity.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class UserRepositorySpec extends Specification {

    @Autowired
    UserRepository userRepository

    final userAdmin = new UserEntity.Builder()
            .name("admin")
            .email("admin@kiesoft.es")
            .password("Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def "create an UserEntity without id (UUID) and without Roles"() {
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

    def "will find by name (ignore case) when it exists"() {
        given:
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findByNameIgnoreCase("aDmiN")

        then:
        actual.isPresent()
    }

    def "will not find by name (ignore case) when it does not exist"() {
        when:
        final actual = userRepository.findByNameIgnoreCase("aDmiN")

        then:
        !actual.isPresent()
    }

    def "will find by email (ignore case) when it exists"() {
        given:
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findByEmailIgnoreCase("aDmiN@KIEsOfT.es")

        then:
        actual.isPresent()
    }

    def "will not find by email (ignore case) when it does not exist"() {
        when:
        final actual = userRepository.findByEmailIgnoreCase("aDmiN@KIEsOfT.es")

        then:
        !actual.isPresent()
    }

    def "will find by email (ignore case) and password when it exists"() {
        given:
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findByEmailIgnoreCaseAndPassword("aDmiN@KIEsOfT.es", "Betis")

        then:
        actual.isPresent()
    }

    def "will not find by email (ignore case) and password when it does not exist"() {
        when:
        final actual = userRepository.findByEmailIgnoreCaseAndPassword("aDmiN@KIEsOfT.es", "Betis")

        then:
        !actual.isPresent()
    }

    def "will find by id when it exists"() {
        given:
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findById(userAdmin.getId())

        then:
        actual.isPresent()
    }

    def "will not find by id when it does not exist"() {
        when:
        final actual = userRepository.findById(UUID.randomUUID())

        then:
        !actual.isPresent()
    }

}
