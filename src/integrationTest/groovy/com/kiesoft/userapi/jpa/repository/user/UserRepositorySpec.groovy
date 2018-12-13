package com.kiesoft.userapi.jpa.repository.user

import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.jpa.entity.role.RoleEntity
import com.kiesoft.userapi.jpa.entity.user.UserEntity
import com.kiesoft.userapi.jpa.repository.role.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class UserRepositorySpec extends Specification {

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    def userAdmin = new UserEntity.Builder()
            .name("admin")
            .email("admin@kiesoft.es")
            .password("Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def roleAdmin = new RoleEntity.Builder()
            .name(TestDataService.ROLE_ADMIN)
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

    def "add RoleEntity to an UserEntity"() {
        given: "Create UserEntity without roles"
        userAdmin=userRepository.save(userAdmin)

        and: "Create RoleEntity"
        roleAdmin=roleRepository.save(roleAdmin)

        and: "add RoleEntity"
        userAdmin.addRole(roleAdmin)

        and: "save the UserEntity"
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findById(userAdmin.getId())

        then: "UserEntity has got only one RoleEntity"
        actual.isPresent()
        actual.get().getRoles().size() == 1
    }

    def "remove RoleEntity from UserEntity"() {
        given: "Create UserEntity without roles"
        userAdmin=userRepository.save(userAdmin)

        and: "Create RoleEntity"
        roleAdmin=roleRepository.save(roleAdmin)

        and: "add RoleEntity"
        userAdmin.addRole(roleAdmin)

        and: "save the UserEntity"
        userRepository.save(userAdmin)

        and: "Get UserEntity from DB"
        final optionalUserEntity=userRepository.findById(userAdmin.getId())

        and: "Remove RoleEntity"
        optionalUserEntity.get().removeRole(roleAdmin)

        and: "save the UserEntity"
        userRepository.save(optionalUserEntity.get())

        when:
        final actual = userRepository.findById(userAdmin.getId())

        then: "UserEntity does not have RoleEntity"
        actual.isPresent()
        actual.get().getRoles().isEmpty()
    }

    def "will not create a UserEntity when the name already exists"() {
        given:
        userRepository.save(userAdmin)

        and:
        def newUserAdmin = new UserEntity.Builder()
                .name("admin")
                .email("admin@kiesoft.es")
                .password("Betis")
                .enabled(Boolean.TRUE)
                .points(100)
                .build()
        userRepository.save(newUserAdmin)

        when:
        userRepository.findAll()

        then:
        thrown Exception
    }

    def "update attributes when it already exists"() {
        given:
        userRepository.save(userAdmin)

        and: "update attributes"
        userAdmin.setName("Loco")
        userAdmin.setEmail("loco@kiesoft.com")
        userAdmin.setPassword("nuevaPass")
        userAdmin.setEnabled(Boolean.FALSE)
        userAdmin.setPoints(2000)
        userRepository.save(userAdmin)

        when:
        final actual = userRepository.findById(userAdmin.getId())

        then:
        actual.isPresent()
        with(actual.get()) {
            name == userAdmin.getName()
            email == userAdmin.getEmail()
            password == userAdmin.getPassword()
            enabled == userAdmin.getEnabled()
            points == userAdmin.getPoints()
        }
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
