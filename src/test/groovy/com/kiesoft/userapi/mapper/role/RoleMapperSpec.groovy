package com.kiesoft.userapi.mapper.role

import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.jpa.entity.role.RoleEntity
import spock.lang.Specification

class RoleMapperSpec extends Specification {

    final roleMapper = new RoleMapperImpl()

    def "will convert an Entity to DTO"() {
        given:
        final roleEntity = new RoleEntity.Builder()
                .id(UUID.randomUUID())
                .name("ROLE_BETIS")
                .build()

        when:
        final roleDTO = roleMapper.asDTO(roleEntity)

        then:
        with(roleDTO) {
            id == roleEntity.id
            name == roleEntity.name
        }
    }

    def "will convert a DTO to Entity"() {
        given:
        final roleDTO = new RoleDTO.Builder()
                .id(UUID.randomUUID())
                .name("ROLE_BETIS")
                .build()

        when:
        final roleEntity = roleMapper.asEntity(roleDTO)

        then:
        with(roleEntity) {
            id == roleDTO.id
            name == roleDTO.name
        }
    }

}
