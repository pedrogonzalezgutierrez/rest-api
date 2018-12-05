package com.kiesoft.userapi.validator.role

import com.kiesoft.userapi.dto.role.CreateRoleDTO
import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.service.role.RoleService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import org.springframework.core.env.Environment
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class CreateRoleDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final env = Mock(Environment)
    final roleService = Mock(RoleService)
    final createRoleDTOValidator = new CreateRoleDTOValidator(validatorHelper, env, roleService)

    final roleDTO = new RoleDTO.Builder()
            .id(UUID.randomUUID())
            .name("ROLE_ADMIN")
            .build()

    def "validation success"() {
        given:
        final createRoleDTO = new CreateRoleDTO.Builder()
                .name("ROLE_ADMIN")
                .build()
        final errors = new BeanPropertyBindingResult(createRoleDTO, "createRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        and:
        roleService.findByName(_ as String) >> Optional.empty()

        when:
        createRoleDTOValidator.validate(createRoleDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing name"() {
        given:
        final createRoleDTO = new CreateRoleDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(createRoleDTO, "createRoleDTO")

        when:
        createRoleDTOValidator.validate(createRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when name too small"() {
        given:
        final createRoleDTO = new CreateRoleDTO.Builder()
                .name("ROLE")
                .build()
        final errors = new BeanPropertyBindingResult(createRoleDTO, "createRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        when:
        createRoleDTOValidator.validate(createRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when name too big"() {
        given:
        final createRoleDTO = new CreateRoleDTO.Builder()
                .name("ROLE_SUPER_LONG")
                .build()
        final errors = new BeanPropertyBindingResult(createRoleDTO, "createRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        when:
        createRoleDTOValidator.validate(createRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when role already exists"() {
        given:
        final createRoleDTO = new CreateRoleDTO.Builder()
                .name("ROLE_ADMIN")
                .build()
        final errors = new BeanPropertyBindingResult(createRoleDTO, "createRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        and:
        roleService.findByName(_ as String) >> Optional.of(roleDTO)

        when:
        createRoleDTOValidator.validate(createRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

}
