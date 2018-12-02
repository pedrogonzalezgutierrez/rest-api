package com.kiesoft.restapi.validator.user

import com.kiesoft.customer.dto.user.CreateUserDTO
import com.kiesoft.customer.dto.user.UserDTO
import com.kiesoft.customer.service.user.UserService
import com.kiesoft.customer.validator.DefaultValidatorHelper
import org.springframework.core.env.Environment
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class CreateUserDTOValidatorSpec extends Specification {

    final env = Mock(Environment)
    final validatorHelper = new DefaultValidatorHelper()
    final userService = Mock(UserService)
    final createUserDTOValidator = new CreateUserDTOValidator(validatorHelper, env, userService)

    def "validation success"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 10
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 10

        and:
        userService.findByName(_) >> Optional.empty()

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing name"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing password"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing user and password"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when user too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("aaa")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 15
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when user too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 15
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.hasErrors()
    }

    def "validation fails when password too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .password("aaa")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 15
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when password too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .password("Betis Betis Balompie")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 15
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when username and password are too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("rbb")
                .password("loco")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 15
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when username and password are too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .password("Real Betis Balompie")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 15
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when username is already taken"() {
        given:
        final name = "pEDROLA"
        final createUserDTO = new CreateUserDTO.Builder()
                .name(name)
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        final userDTO = new UserDTO.Builder()
                .id(UUID.randomUUID())
                .name(name)
                .password("Betis")
                .enabled(Boolean.TRUE)
                .points(100)
                .build()

        userService.findByName(name) >> Optional.of(userDTO)

        and:
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_LENGTH_MAX) >> 15
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.hasErrors()
    }

}
