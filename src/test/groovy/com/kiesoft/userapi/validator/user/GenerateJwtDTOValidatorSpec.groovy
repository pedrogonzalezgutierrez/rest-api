package com.kiesoft.userapi.validator.user

import com.kiesoft.userapi.dto.user.CreateUserDTO
import com.kiesoft.userapi.dto.user.GenerateJwtDTO
import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.service.user.UserService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import org.springframework.core.env.Environment
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification

class GenerateJwtDTOValidatorSpec extends Specification {

    final env = Mock(Environment)
    final validatorHelper = new DefaultValidatorHelper()
    final userService = Mock(UserService)
    final generateJwtDTOValidator = new GenerateJwtDTOValidator(validatorHelper, env, userService)

    def "validation success"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 10

        and:
        final userDTO = new UserDTO.Builder()
                .id(UUID.randomUUID())
                .name("pEDROLA")
                .email("pedro@email.com")
                .password("Betis")
                .enabled(Boolean.TRUE)
                .points(100)
                .build()

        and:
        userService.findByEmailAndPassword(_ as String, _ as String) >> Optional.of(userDTO)

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        !errors.hasErrors()
    }

    def "validation fails when missing email"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder()
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing password"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder()
                .email("pedro@email.com")
                .build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when missing email and password"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder().build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        errors.getErrorCount() == 2
    }

    def "validation fails when password too small"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder()
                .email("pedro@email.com")
                .password("aa")
                .build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when password too big"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder()
                .email("pedro@email.com")
                .password("Real Betis Balompie")
                .build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when email is invalid"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder()
                .email("pedro@")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        errors.getErrorCount() == 1
    }

    def "validation fails when email and password does not exist in database"() {
        given:
        final generateJwtDTO = new GenerateJwtDTO.Builder()
                .email("pedro@email.com")
                .password("Betis")
                .build()
        final errors = new BeanPropertyBindingResult(generateJwtDTO, "generateJwtDTO")

        and:
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MIN) >> 5
        env.getProperty(CreateUserDTOValidator.USER_PASSWORD_MAX) >> 15

        and:
        userService.findByEmailAndPassword(_ as String, _ as String) >> Optional.empty()

        when:
        generateJwtDTOValidator.validate(generateJwtDTO, errors)

        then:
        errors.getErrorCount() == 2
    }


}