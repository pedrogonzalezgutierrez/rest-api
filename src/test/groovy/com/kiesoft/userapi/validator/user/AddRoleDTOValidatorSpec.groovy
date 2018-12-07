package com.kiesoft.userapi.validator.user


import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.dto.user.AddRoleDTO
import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.service.role.RoleService
import com.kiesoft.userapi.service.user.UserService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import com.kiesoft.userapi.validator.role.RoleProperties
import org.springframework.core.env.Environment
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class AddRoleDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final env = Mock(Environment)
    final roleService = Mock(RoleService)
    final userService = Mock(UserService)
    final addRoleDTOValidator = new AddRoleDTOValidator(validatorHelper, env, roleService, userService)

    final roleDTO = new RoleDTO.Builder()
            .id(UUID.randomUUID())
            .name("ROLE_ADMIN")
            .build()

    final userDTO = new UserDTO.Builder()
            .id(UUID.randomUUID())
            .name("pEDROLA")
            .email("pedrola@kiesoft.com")
            .password("Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def "validation success"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and: "Role exists"
        roleService.findById(UUID.fromString(addRoleDTO.getIdRole())) >> Optional.of(roleDTO)

        and: "User exists"
        userService.findById(UUID.fromString(addRoleDTO.getIdUser())) >> Optional.of(userDTO)

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing idUser"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing idRole"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing idRole and idUser"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when idUser is not an UUID"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser("Not an UUID")
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when idRole is not an UUID"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole("Not an UUID")
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when neither idRole nor idUser are UUID"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idRole("Not an UUID")
                .idUser("Not an UUID")
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when Role does not exist"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and: "Role does not exist"
        roleService.findById(UUID.fromString(addRoleDTO.getIdRole())) >> Optional.empty()

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when User does not exist"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and: "Role exists"
        roleService.findById(UUID.fromString(addRoleDTO.getIdRole())) >> Optional.of(roleDTO)

        and: "User does not exist"
        userService.findById(UUID.fromString(addRoleDTO.getIdUser())) >> Optional.empty()

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when User already contains the Role"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(UUID.randomUUID().toString())
                .build()
        final errors = new BeanPropertyBindingResult(addRoleDTO, "addRoleDTO")

        and: "Role exists"
        roleService.findById(UUID.fromString(addRoleDTO.getIdRole())) >> Optional.of(roleDTO)

        and: "User does not exist"
        userDTO.getRoles().add(roleDTO)
        userService.findById(UUID.fromString(addRoleDTO.getIdUser())) >> Optional.of(userDTO)

        when:
        addRoleDTOValidator.validate(addRoleDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

}
