package com.kiesoft.userapi.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.controller.error.ApiValidationExceptionHandler
import com.kiesoft.userapi.dto.user.CreateUserDTO
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

import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER

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

    def "user created when it does not exist and validation successful"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .password("betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "user not created when missing name"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .email("admin@correo.es")
                .password("admin")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when missing email"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("admin")
                .password("admin")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when missing password"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("admin")
                .email("admin@correo.es")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when missing name, email and password"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder().build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when name too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("a")
                .email("admin@correo.es")
                .password("Betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when name too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .email("admin@correo.es")
                .password("betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when password too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("admin@correo.es")
                .password("1")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when password too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("admin@correo.es")
                .password("Real BEtis Balompie")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when name and password too small"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("aa")
                .email("admin@correo.es")
                .password("bb")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when name and password too big"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("Pedro Gonzalez Gutierrez")
                .email("admin@correo.es")
                .password("Real Betis Balompie")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when email is invalid"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("pEDROLA")
                .email("admin@")
                .password("Betis")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when it already exist"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("admin")
                .email("newadmin@kiesoft.com")
                .password("admin")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    def "user not created when it does not exist but email already exist"() {
        given:
        final createUserDTO = new CreateUserDTO.Builder()
                .name("sucolega")
                .email("admin@kiesoft.com")
                .password("sucolega")
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + "/")
                .content(objectMapper.writeValueAsString(createUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
    }


}
