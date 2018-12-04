package com.kiesoft.userapi.jpa.repository.role

import com.kiesoft.userapi.jpa.entity.role.RoleEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class RoleRepositorySpec extends Specification {

    @Autowired
    RoleRepository roleRepository

    final roleAdmin = new RoleEntity.Builder()
            .name("ROLE_ADMIN")
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

    def "will find by name (ignore case) when it exists"() {
        given:
        roleRepository.save(roleAdmin)

        when:
        final actual = roleRepository.findByNameIgnoreCase("rOlE_AdMiN")

        then:
        actual.isPresent()
    }

    def "will not find by name (ignore case) when it does not exist"() {
        when:
        final actual = roleRepository.findByNameIgnoreCase("rOlE_AdMiN")

        then:
        !actual.isPresent()
    }

}
