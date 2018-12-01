package com.kiesoft.customer.mapper.user

import com.kiesoft.customer.jpa.entity.role.RoleEntity
import com.kiesoft.customer.jpa.entity.user.UserEntity
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.tuple;

class UserMapperSpec extends Specification {

    final userConverter = new UserMapperImpl()

    def "will convert an Entity to DTO"() {
        given:
        final firstRole = new RoleEntity.Builder()
                .id(UUID.randomUUID())
                .name("ROLE_ADMIN")
                .build()

        final secondRole = new RoleEntity.Builder()
                .id(UUID.randomUUID())
                .name("ROLE_EDITOR")
                .build()

        final userEntity = new UserEntity.Builder()
                .id(UUID.randomUUID())
                .name("Pedro")
                .password("Real Betis")
                .enabled(Boolean.TRUE)
                .roles([firstRole, secondRole])
                .points(100)
                .build()

        when:
        final userDTO = userConverter.asDTO(userEntity)

        then:
        with(userDTO) {
            id == userEntity.id
            password == userEntity.password
            enabled == userEntity.enabled
            points == userEntity.points
        }
        assertThat(userDTO.roles)
                .extracting("id", "name")
                .contains(
                tuple(firstRole.id, firstRole.name),
                tuple(secondRole.id, secondRole.name))
    }

}
