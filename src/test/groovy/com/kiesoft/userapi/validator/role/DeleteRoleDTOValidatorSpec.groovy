package com.kiesoft.userapi.validator.role


import com.kiesoft.userapi.dto.role.DeleteRoleDTO
import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.service.role.RoleService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import org.springframework.core.env.Environment
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class DeleteRoleDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final env = Mock(Environment)
    final roleService = Mock(RoleService)
    final deleteRoleDTOValidator = new DeleteRoleDTOValidator(validatorHelper, env, roleService)

    def "validation success"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name("ROLE_ADMIN")
                .build()
        final errors = new BeanPropertyBindingResult(deleteRoleDTO, "deleteRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        and:
        final roleDTO = new RoleDTO.Builder()
                .id(UUID.randomUUID())
                .name("ROLE_ADMIN")
                .build()
        roleService.findByName(_ as String) >> Optional.of(roleDTO)

        when:
        deleteRoleDTOValidator.validate(deleteRoleDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing name"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(deleteRoleDTO, "deleteRoleDTO")

        when:
        deleteRoleDTOValidator.validate(deleteRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when name too small"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name("ROLE")
                .build()
        final errors = new BeanPropertyBindingResult(deleteRoleDTO, "deleteRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        when:
        deleteRoleDTOValidator.validate(deleteRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when name too big"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name("ROLE_SUPER_LONG")
                .build()
        final errors = new BeanPropertyBindingResult(deleteRoleDTO, "deleteRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        when:
        deleteRoleDTOValidator.validate(deleteRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when role does not exists"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name("ROLE_ADMIN")
                .build()
        final errors = new BeanPropertyBindingResult(deleteRoleDTO, "deleteRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        and:
        roleService.findByName(_ as String) >> Optional.empty()

        when:
        deleteRoleDTOValidator.validate(deleteRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

}
