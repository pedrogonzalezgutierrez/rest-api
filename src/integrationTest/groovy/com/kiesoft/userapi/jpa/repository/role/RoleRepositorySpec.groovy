package com.kiesoft.userapi.jpa.repository.role

import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.jpa.entity.role.RoleEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class RoleRepositorySpec extends Specification {

    @Autowired
    RoleRepository roleRepository

    final roleAdmin = new RoleEntity.Builder()
            .name(TestDataService.ROLE_ADMIN)
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
