package com.kiesoft.userapi.jpa.repository.user

import com.kiesoft.userapi.jpa.entity.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class UserRepositorySpec extends Specification {

    @Autowired
    private UserRepository userRepository

    final user1 = new UserEntity.Builder()
            .name("Pedro")
            .password("Real Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def "save an UserEntity without id (UUID) and without Roles"() {
        given:
        userRepository.save(user1)

        when:
        final actual = userRepository.findAll()

        then: "there is only one user"
        actual.size() == 1

        and: "fields are saved successfully"
        with(actual.getAt(0)) {
            id != null
            name == user1.name
            password == user1.password
            enabled == user1.enabled
            points == user1.points
        }

        and: "there is no roles"
        actual.getAt(0).getRoles().isEmpty()
    }

    def "find by name"() {
        given:
        userRepository.save(user1)

        when:
        final actual = userRepository.findByName(user1.getName())

        then:
        actual.isPresent()
        with(actual.get()) {
            id != null
            name == user1.name
            password == user1.password
            enabled == user1.enabled
            points == user1.points
        }
    }

}
