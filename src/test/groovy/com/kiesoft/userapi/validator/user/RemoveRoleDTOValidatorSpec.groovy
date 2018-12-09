package com.kiesoft.userapi.validator.user

import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.dto.user.AddRoleDTO
import com.kiesoft.userapi.dto.user.RemoveRoleDTO
import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.service.role.RoleService
import com.kiesoft.userapi.service.user.UserService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class RemoveRoleDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final roleService = Mock(RoleService)
    final userService = Mock(UserService)
    final removeRoleDTOValidator = new RemoveRoleDTOValidator(validatorHelper, roleService, userService)

    final roleDTO = new RoleDTO.Builder()
            .id(UUID.randomUUID())
            .name("ROLE_ADMIN")
            .build()

    def "validation success"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        and: "Role exists"
        roleService.findById(UUID.fromString(removeRoleDTO.getIdRole())) >> Optional.of(roleDTO)

        and: "User exists"
        final userDTO = new UserDTO.Builder()
                .id(UUID.randomUUID())
                .name("pEDROLA")
                .email("pedrola@kiesoft.com")
                .password("Betis")
                .enabled(Boolean.TRUE)
                .points(100)
                .roles([roleDTO])
                .build()
        userService.findById(UUID.fromString(removeRoleDTO.getIdUser())) >> Optional.of(userDTO)

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing idUser"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing idRole"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing idRole and idUser"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when idUser is not an UUID"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser("Not an UUID")
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when idRole is not an UUID"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole("Not an UUID")
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when neither idRole nor idUser are UUID"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idRole("Not an UUID")
                .idUser("Not an UUID")
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when Role does not exist"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        and: "Role does not exist"
        roleService.findById(UUID.fromString(removeRoleDTO.getIdRole())) >> Optional.empty()

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when User does not exist"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        and: "Role exists"
        roleService.findById(UUID.fromString(removeRoleDTO.getIdRole())) >> Optional.of(roleDTO)

        and: "User does not exist"
        userService.findById(UUID.fromString(removeRoleDTO.getIdUser())) >> Optional.empty()

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when User does not contain the Role"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(removeRoleDTO, "removeRoleDTO")

        and: "Role exists"
        roleService.findById(UUID.fromString(removeRoleDTO.getIdRole())) >> Optional.of(roleDTO)

        and: "User does not contain the role"
        final userDTO = new UserDTO.Builder()
                .id(UUID.randomUUID())
                .name("pEDROLA")
                .email("pedrola@kiesoft.com")
                .password("Betis")
                .enabled(Boolean.TRUE)
                .points(100)
                .build()
        userService.findById(UUID.fromString(removeRoleDTO.getIdUser())) >> Optional.of(userDTO)

        when:
        removeRoleDTOValidator.validate(removeRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

}
