package com.kiesoft.restapi.jpa.repository.user


import com.kiesoft.customer.jpa.entity.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class UserRepositorySpec extends Specification {

    @Autowired
    private UserRepository userRepository

    def "will save an UserEntity without Roles"() {
        given:
        final user1 = new UserEntity.Builder()
                .id(UUID.randomUUID())
                .name("Pedro")
                .password("Real Betis")
                .enabled(Boolean.TRUE)
                .points(100)
                .build()

        and:
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

    def "will find by name"() {
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
