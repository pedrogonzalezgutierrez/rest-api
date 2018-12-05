package com.kiesoft.userapi.validator.user

import com.kiesoft.userapi.dto.user.CreateUserDTO
import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.service.user.UserService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
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
                .email("pedro@email.com")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 10
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 10

        and:
        userService.findByName(_ as String) >> Optional.empty()
        userService.findByEmail(_ as String) >> Optional.empty()

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing name"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing email"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
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
                .email("pedro@email.com")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing name, email and password"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 3
    }

    def "validation fails when name too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("aaa")
                .email("pedro@email.com")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when name too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .email("pedro@email.com")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.hasErrors()
    }

    def "validation fails when password too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("pedro@email.com")
                .password("aaa")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when password too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("pedro@email.com")
                .password("Betis Betis Balompie")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when username and password are too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("rbb")
                .email("pedro@email.com")
                .password("loco")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when username and password are too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .email("pedro@email.com")
                .password("Real Betis Balompie")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when email is invalid"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("email@")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when username '#name' is already taken"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name(name)
                .email("pedrola@kiesoft.com")
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
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1

        where:
        name      || _
        "pEDROLA" || _
        "pedrola" || _
    }

    def "validation fails when email is already taken"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email(email)
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(createUserDTO, "createUserDTO")

        and:
        final userDTO = new UserDTO.Builder()
                .id(UUID.randomUUID())
                .name("pEDROLA")
                .email(email)
                .password("Betis")
                .enabled(Boolean.TRUE)
                .points(100)
                .build()

        userService.findByName(_ as String) >> Optional.empty()
        userService.findByEmail(email) >> Optional.of(userDTO)

        and:
        env.getProperty(UserProperties.USER_LENGTH_MIN) >> 5
        env.getProperty(UserProperties.USER_LENGTH_MAX) >> 15
        env.getProperty(UserProperties.USER_PASSWORD_MIN) >> 5
        env.getProperty(UserProperties.USER_PASSWORD_MAX) >> 15

        when:
        createUserDTOValidator.validate(createUserDTO, errors)

        then:
        errors.getErrorCount() == 1

        where:
        email               || _
        "pEDROLA@correo.es" || _
        "pedrola@CORREO.es" || _

    }

}
