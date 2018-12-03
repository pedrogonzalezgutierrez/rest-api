package com.kiesoft.userapi.validator.user

import com.kiesoft.userapi.dto.user.ChangePasswordDTO
import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.service.user.UserService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import org.springframework.core.env.Environment
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class ChangePasswordDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final env = Mock(Environment)
    final userService = Mock(UserService)
    final changePasswordDTOValidator = new ChangePasswordDTOValidator(validatorHelper, env, userService)

    final userDTO = new UserDTO.Builder()
            .id(UUID.randomUUID())
            .name("pEDROLA")
            .email("pedro@email.com")
            .password("Betis")
            .enabled(Boolean.TRUE)
            .points(100)
            .build()

    def "validation success"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 10

        and:
        userService.findByEmailAndPassword(_ as String, _ as String) >> Optional.of(userDTO)

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing email"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .password("Betis")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing password"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing newPassword"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing email, password and newPassword"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 3
    }

    def "validation fails when password too small"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("a")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when password too big"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Real Betis Balompie")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when newPassword too small"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .newPassword("a")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when newPassword too big"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .newPassword("Real Betis Balompie")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when password is equal to newPassword"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .newPassword("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when email is invalid"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@")
                .password("Betis")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when valid email and password but user is not enabled"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 10

        and:
        final userDTO = new UserDTO.Builder()
                .id(UUID.randomUUID())
                .name("pEDROLA")
                .email("pedro@email.com")
                .password("Betis")
                .enabled(Boolean.FALSE)
                .points(100)
                .build()
        userService.findByEmailAndPassword(_ as String, _ as String) >> Optional.of(userDTO)

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when invalid email and password"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .newPassword("Heliopolis")
                .build()
        final errors = new BeanPropertyBindingResult(changePasswordDTO, "changePasswordDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 10

        and:
        userService.findByEmailAndPassword(_ as String, _ as String) >> Optional.empty()

        when:
        changePasswordDTOValidator.validate(changePasswordDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

}
