package com.kiesoft.userapi.validator.user


import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.dto.user.AddRoleDTO
import com.kiesoft.userapi.service.role.RoleService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import com.kiesoft.userapi.validator.role.RoleProperties
import org.springframework.core.env.Environment
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class AddRoleDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final env = Mock(Environment)
    final roleService = Mock(RoleService)
    final addRoleDTOValidator = new AddRoleDTOValidator(validatorHelper, env, roleService)

    final roleDTO = new RoleDTO.Builder()
            .id(UUID.randomUUID())
            .name("ROLE_ADMIN")
            .build()

    def "validation success"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .name("ROLE_ADMIN")
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        and:
        roleService.findByName(_ as String) >> Optional.of(roleDTO)

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing name"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when name too small"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .name("ROLE")
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when name too big"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .name("ROLE_SUPER_LONG")
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when role does not exist"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .name("ROLE_ADMIN")
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and:
        env.getProperty(RoleProperties.NAME_LENGTH_MIN) >> 5
        env.getProperty(RoleProperties.NAME_LENGTH_MAX) >> 10

        and:
        roleService.findByName(_ as String) >> Optional.empty()

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

}
