package com.kiesoft.userapi.jpa.repository.role

import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.jpa.entity.role.RoleEntity
import com.kiesoft.userapi.jpa.entity.user.UserEntity
import com.kiesoft.userapi.jpa.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class RoleRepositorySpec extends Specification {

    @Autowired
    RoleRepository roleRepository

    @Autowired
    UserRepository userRepository

    def roleAdmin = new RoleEntity.Builder()
            .name(TestDataService.ROLE_ADMIN)
            .build()

    def userAdmin = new UserEntity.Builder()
            .name("admin")
            .email("admin@kiesoft.es")
            .password("Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def "create a RoleEntity without id (UUID)"() {
        given:
        roleRepository.save(roleAdmin)

        when:
        final actual = roleRepository.findAll()

        then: "there is only one role"
        actual.size() == 1

        and: "fields are saved successfully"
        with(actual.getAt(0)) {
            id != null
            name == roleAdmin.name
        }
    }

    def "will not create a RoleEntity when the name already exists"() {
        given:
        roleRepository.save(roleAdmin)

        and:
        final newRoleAdmin = new RoleEntity.Builder()
                .name(TestDataService.ROLE_ADMIN)
                .build()
        roleRepository.save(newRoleAdmin)

        when:
        roleRepository.findAll()

        then:
        thrown Exception
    }

    def "will delete a RoleEntity when is not used by any UserEntity"() {
        given:
        roleAdmin=roleRepository.save(roleAdmin)

        when:
        roleRepository.deleteById(roleAdmin.getId())

        then:
        roleRepository.findAll().isEmpty()
    }

    def "will not delete a RoleEntity when is used by an UserEntity"() {
        given: "Create UserEntity without roles"
        userAdmin=userRepository.save(userAdmin)

        and: "Create RoleEntity"
        roleAdmin=roleRepository.save(roleAdmin)

        and: "add RoleEntity"
        userAdmin.addRole(roleAdmin)

        and: "save the UserEntity"
        userRepository.save(userAdmin)

        and: "delete the RoleEntity"
        roleRepository.deleteById(roleAdmin.getId())

        when:
        userRepository.findAll()

        then:
        thrown Exception
    }

    def "will find by name (ignore case) when it exists"() {
        given:
        roleRepository.save(roleAdmin)

        when:
        final actual = roleRepository.findByNameIgnoreCase(TestDataService.ROLE_ADMIN.toLowerCase())

        then:
        actual.isPresent()
    }

    def "will not find by name (ignore case) when it does not exist"() {
        when:
        final actual = roleRepository.findByNameIgnoreCase(TestDataService.ROLE_ADMIN.toLowerCase())

        then:
        !actual.isPresent()
    }

    def "will find by id when it exists"() {
        given:
        roleRepository.save(roleAdmin)

        when:
        final actual = roleRepository.findById(roleAdmin.getId())

        then:
        actual.isPresent()
    }

    def "will not find by id when it does not exist"() {
        when:
        final actual = roleRepository.findById(UUID.randomUUID())

        then:
        !actual.isPresent()
    }

}
