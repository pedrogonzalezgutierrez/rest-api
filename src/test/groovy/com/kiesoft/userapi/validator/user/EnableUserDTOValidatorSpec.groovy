package com.kiesoft.userapi.validator.user

import com.kiesoft.userapi.dto.user.EnableUserDTO
import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.service.user.UserService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class EnableUserDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final userService = Mock(UserService)
    final enableUserDTOValidator = new EnableUserDTOValidator(validatorHelper, userService)

    final userDTO = new UserDTO.Builder()
            .id(UUID.randomUUID())
            .name("pEDROLA")
            .email("pedro@kiesoft.com")
            .password("Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def "validation success"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
            .email("pedro@kiesoft.com")
            .enable(Boolean.FALSE)
            .build()
        final errors = new BeanPropertyBindingResult(enableUserDTO, "enableUserDTO")

        and:
        userService.findByEmail(_ as String) >> Optional.of(userDTO)

        when:
        enableUserDTOValidator.validate(enableUserDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing email"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .enable(Boolean.FALSE)
                .build()
        final errors = new BeanPropertyBindingResult(enableUserDTO, "enableUserDTO")

        when:
        enableUserDTOValidator.validate(enableUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing enabled"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("pedro@kiesoft.com")
                .build()
        final errors = new BeanPropertyBindingResult(enableUserDTO, "enableUserDTO")

        when:
        enableUserDTOValidator.validate(enableUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing email and enabled"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(enableUserDTO, "enableUserDTO")

        when:
        enableUserDTOValidator.validate(enableUserDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when email is invalid"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("pedro@kiesoft")
                .enable(Boolean.FALSE)
                .build()
        final errors = new BeanPropertyBindingResult(enableUserDTO, "enableUserDTO")

        when:
        enableUserDTOValidator.validate(enableUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when email not found"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("pedro@kiesoft.com")
                .enable(Boolean.FALSE)
                .build()
        final errors = new BeanPropertyBindingResult(enableUserDTO, "enableUserDTO")

        and:
        userService.findByEmail(_ as String) >> Optional.empty()

        when:
        enableUserDTOValidator.validate(enableUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails because value already got that value"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("pedro@kiesoft.com")
                .enable(Boolean.TRUE)
                .build()
        final errors = new BeanPropertyBindingResult(enableUserDTO, "enableUserDTO")

        and:
        userService.findByEmail(_ as String) >> Optional.of(userDTO)

        when:
        enableUserDTOValidator.validate(enableUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

}
