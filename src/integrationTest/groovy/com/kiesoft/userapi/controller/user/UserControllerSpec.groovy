package com.kiesoft.userapi.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.controller.error.ApiValidationExceptionHandler
import com.kiesoft.userapi.dto.user.CreateUserDTO
import com.kiesoft.userapi.dto.user.GenerateJwtDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.kiesoft.userapi.controller.user.AbstractUserController.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "integrationTest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    UserController userController

    @Autowired
    TestDataService testDataService

    final objectMapper = new ObjectMapper();

    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController, new ApiValidationExceptionHandler()).build()
    }

    def "createNewUser: user created when it does not exist and validation successful"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("pedrola@email.es")
                .password("betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "createNewUser: user not created when missing name"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .email("admin@correo.es")
                .password("admin")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when missing email"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("admin")
                .password("admin")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when missing password"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("admin")
                .email("admin@correo.es")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when missing name, email and password"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder().build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when name too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("a")
                .email("admin@correo.es")
                .password("Betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when name too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .email("admin@correo.es")
                .password("betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when password too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("admin@correo.es")
                .password("1")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when password too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("admin@correo.es")
                .password("Real BEtis Balompie")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when name and password too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("aa")
                .email("admin@correo.es")
                .password("bb")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when name and password too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .email("admin@correo.es")
                .password("Real Betis Balompie")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when email is invalid"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("admin@")
                .password("Betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "createNewUser: user not created when it already exist (ignore case)"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name(name)
                .email("newadmin@kiesoft.com")
                .password("admin")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        where:
        name                  || _
        "admin"               || _
        "admin".toUpperCase() || _
    }

    def "createNewUser: user not created when it does not exist but email already exist (ignore case)"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("sucolega")
                .email(email)
                .password("sucolega")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_CREATE)
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        where:
        email                             || _
        "admin@kiesoft.com"               || _
        "admin@kiesoft.com".toUpperCase() || _
    }

    def "retrieveJWT: token generated when is valid credentials and validation successful"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("admin@kiesoft.com")
                .password("admin")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "retrieveJWT: token not generated when missing email"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .password("admin")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when missing password"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("admin@kiesoft.com")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when missing email and password"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder().build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when password too small"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("admin@kiesoft.com")
                .password("a")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when password too big"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("admin@kiesoft.com")
                .password("Real Betis Balompie")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when email is not an email"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("@kiesoft.com")
                .password("admin")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when email invalid"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("wrong@email.com")
                .password("admin")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when password invalid"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("admin@kiesoft.com")
                .password("wrongPassword")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when email and password invalid"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("wrong@email.com")
                .password("wrongPassword")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "retrieveJWT: token not generated when email and password are correct but user not enabled"() {
        given:
        final generateJWTDTO = new GenerateJwtDTO.Builder()
                .email("sucolega@kiesoft.com")
                .password("sucolega")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_JWT)
                .content(objectMapper.writeValueAsString(generateJWTDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }


}
