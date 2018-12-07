package com.kiesoft.userapi.service.role

import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.exception.PersistenceProblemException
import com.kiesoft.userapi.jpa.entity.role.RoleEntity
import com.kiesoft.userapi.jpa.repository.role.RoleRepository
import com.kiesoft.userapi.mapper.role.RoleMapper
import spock.lang.Specification

class DefaultRoleServiceSpec extends Specification {

    final roleRepository = Mock(RoleRepository)
    final roleMapper = Mock(RoleMapper)
    final defaultRoleService = new DefaultRoleService(roleRepository, roleMapper)

    final name = "ROLE_ADMIN"

    final roleDTO = new RoleDTO.Builder()
            .name(name)
            .build()

    final roleEntity = new RoleEntity.Builder()
            .name(name)
            .build()

    UUID idRole = UUID.randomUUID()

    final savedDTO = new RoleDTO.Builder()
            .id(idRole)
            .name(name)
            .build()

    final savedEntity = new RoleEntity.Builder()
            .id(idRole)
            .name(name)
            .build()

    def "create a role"() {
        given:
        roleMapper.asEntity(roleDTO) >> roleEntity
        roleRepository.save(roleEntity) >> savedEntity
        roleMapper.asDTO(savedEntity) >> savedDTO

        when:
        final actual = defaultRoleService.save(roleDTO)

        then:
        actual == savedDTO
    }

    def "error creating a role when persisting DB"() {
        given:
        roleMapper.asEntity(roleDTO) >> roleEntity
        roleRepository.save(roleEntity) >> { throw new RuntimeException() }

        when:
        defaultRoleService.save(roleDTO)

        then:
        thrown PersistenceProblemException
    }

    def "findByName: role does not exist"() {
        given:
        roleRepository.findByNameIgnoreCase(name) >> Optional.empty()

        when:
        final optionalUserDTO = defaultRoleService.findByName(name)

        then:
        !optionalUserDTO.isPresent()
    }

    def "findByName: role exists"() {
        given:
        roleRepository.findByNameIgnoreCase(name) >> Optional.of(savedEntity)
        roleMapper.asDTO(savedEntity) >> savedDTO

        when:
        final optionalRoleDTO = defaultRoleService.findByName(name)

        then:
        optionalRoleDTO.isPresent()
    }

    def "findById: it does not exist"() {
        given:
        roleRepository.findById(idRole) >> Optional.empty()

        when:
        final optionalRoleDTO = defaultRoleService.findById(idRole)

        then:
        !optionalRoleDTO.isPresent()
    }

    def "findById: it exists"() {
        given:
        roleRepository.findById(idRole) >> Optional.of(savedEntity)
        roleMapper.asDTO(savedEntity) >> savedDTO

        when:
        final optionalRoleDTO = defaultRoleService.findById(idRole)

        then:
        optionalRoleDTO.isPresent()
    }

}
