package com.kiesoft.userapi.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.controller.error.ApiErrorMessage
import com.kiesoft.userapi.controller.error.ApiErrorsView
import com.kiesoft.userapi.controller.error.ApiValidationExceptionHandler
import com.kiesoft.userapi.dto.user.AddRoleDTO
import com.kiesoft.userapi.dto.user.ChangePasswordDTO
import com.kiesoft.userapi.dto.user.CreateUserDTO
import com.kiesoft.userapi.dto.user.EnableUserDTO
import com.kiesoft.userapi.dto.user.GenerateJwtDTO
import com.kiesoft.userapi.dto.user.RemoveRoleDTO
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
import static org.assertj.core.api.Assertions.assertThat

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

    // -------------------------------------------
    // Create a new user
    // -------------------------------------------

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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode(), ApiErrorMessage.STRING_LENGTH.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode(), ApiErrorMessage.STRING_LENGTH.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.EMAIL_INVALID.getCode())
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getCode())

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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.EMAIL_ALREADY_EXISTS.getCode())

        where:
        email                             || _
        "admin@kiesoft.com"               || _
        "admin@kiesoft.com".toUpperCase() || _
    }

    // -------------------------------------------
    // Generate a JWT
    // -------------------------------------------

    def "generateJWT: token generated when is valid credentials and validation successful"() {
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

    def "generateJWT: token not generated when missing email"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "generateJWT: token not generated when missing password"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "generateJWT: token not generated when missing email and password"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "generateJWT: token not generated when password too small"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "generateJWT: token not generated when password too big"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "generateJWT: token not generated when email is not an email"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.EMAIL_INVALID.getCode())
    }

    def "generateJWT: token not generated when invalid credentials (email)"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getCode())
    }

    def "generateJWT: token not generated when invalid credentials (password)"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getCode())
    }

    def "generateJWT: token not generated when invalid credentials (email and password)"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getCode())
    }

    def "generateJWT: token not generated when credentials (email and password) are correct but user not enabled"() {
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

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.USER_NOT_ENABLED.getCode())
    }

    // -------------------------------------------
    // Change the password of a user
    // -------------------------------------------

    def "changePassword: password is changed successfully"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("pedrola")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "changePassword: password not change because missing email"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .password("pedrola")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "changePassword: password not change because missing password"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "changePassword: password not change because missing newPassword"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("pedrola")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "changePassword: password not change because missing email, password and newPassword"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder().build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "changePassword: password not change because password too small"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("a")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "changePassword: password not change because password too big"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("Real Betis Balompie")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "changePassword: password not change because newPassword too small"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("Betis")
                .newPassword("a")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "changePassword: password not change because newPassword too big"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("Betis")
                .newPassword("Real Betis Balompie")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "changePassword: password not change because is equal to newPassword"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("Heliopolis")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.PASSWORDS_ARE_EQUALS.getCode())
    }

    def "changePassword: password not change because invalid email"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("@kiesoft.com")
                .password("Betis")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.EMAIL_INVALID.getCode())
    }

    def "changePassword: password not change because invalid credentials (email or password)"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("pedrola@kiesoft.com")
                .password("Betis")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getCode())
    }

    def "changePassword: password not change because invalid credentials (email and password) but user not enabled"() {
        given:
        final changePasswordDTO = new ChangePasswordDTO.Builder()
                .email("sucolega@kiesoft.com")
                .password("sucolega")
                .newPassword("Heliopolis")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_UPDATE_PASSWORD)
                .content(objectMapper.writeValueAsString(changePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.USER_NOT_ENABLED.getCode())
    }

    // -------------------------------------------
    // Enable or disable an user
    // -------------------------------------------

    def "enableUser: enabled is changed successfully"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("sucolega@kiesoft.com")
                .enable(Boolean.TRUE)
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER)
                .content(objectMapper.writeValueAsString(enableUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "enableUser: enabled not changed because missing email"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .enable(Boolean.TRUE)
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER)
                .content(objectMapper.writeValueAsString(enableUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "enableUser: enabled not changed because missing enable"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("sucolega@kiesoft.com")
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER)
                .content(objectMapper.writeValueAsString(enableUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.FIELD_REQUIRED.getCode())
    }

    def "enableUser: enabled not changed because missing email and enable"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder().build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER)
                .content(objectMapper.writeValueAsString(enableUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.FIELD_REQUIRED.getCode())

    }

    def "enableUser: enabled not changed because invalid email"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("sucolega@kiesoft")
                .enable(Boolean.TRUE)
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER)
                .content(objectMapper.writeValueAsString(enableUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.EMAIL_INVALID.getCode())
    }

    def "enableUser: enabled not changed because email does not exist"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("wrong@email.com")
                .enable(Boolean.TRUE)
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER)
                .content(objectMapper.writeValueAsString(enableUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.EMAIL_NOT_FOUND.getCode())
    }

    def "enableUser: enabled not changed because is the same value"() {
        given:
        final enableUserDTO = new EnableUserDTO.Builder()
                .email("sucolega@kiesoft.com")
                .enable(Boolean.FALSE)
                .build()

        and:
        testDataService.usersRandomPeople()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.patch(ROUTING_USER_CONTROLLER + ROUTING_USER_ENABLE_USER)
                .content(objectMapper.writeValueAsString(enableUserDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.FIELD_NOT_CHANGED.getCode())
    }

    // -------------------------------------------
    // Add Role to a user
    // -------------------------------------------

    def "addRole: role is added to the user"() {
        given:
        final roleAdmin = testDataService.roleAdmin();
        final userPedrola = testDataService.userPedrola();

        and:
        final addRoleDTO = new AddRoleDTO.Builder()
            .idUser(userPedrola.getId().toString())
            .idRole(roleAdmin.getId().toString())
            .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "addRole: validation fails when missing idUser"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idRole(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "addRole: validation fails when missing idRole"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "addRole: validation fails when missing idUser and idRole"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder().build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "addRole: validation fails when idUser is not an UUID"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser("not an UUID")
                .idRole(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_NOT_UUID.getCode())
    }

    def "addRole: validation fails when idRole is not an UUID"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole("not an UUID")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_NOT_UUID.getCode())
    }

    def "addRole: validation fails when neither idRole nor idUser is not an UUID"() {
        given:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser("not an UUID")
                .idRole("not an UUID")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_NOT_UUID.getCode(), ApiErrorMessage.STRING_NOT_UUID.getCode())
    }

    def "addRole: validation fails when Role does not exist"() {
        given:
        final userPedrola = testDataService.userPedrola();

        and:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(userPedrola.getId().toString())
                .idRole(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.ROLE_NOT_FOUND.getCode())
    }

    def "addRole: validation fails when User does not exist"() {
        given:
        final roleAdmin = testDataService.roleAdmin();

        and:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(roleAdmin.getId().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.USER_NOT_FOUND.getCode())
    }

    def "addRole: validation fails when User already contains the Role"() {
        given:
        final roleAdmin = testDataService.roleAdmin();
        final userAdmin = testDataService.userAdmin();

        and:
        final addRoleDTO = new AddRoleDTO.Builder()
                .idUser(userAdmin.getId().toString())
                .idRole(roleAdmin.getId().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(addRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.USER_ALREADY_HAS_THE_ROLE.getCode())
    }

    // -------------------------------------------
    // Remove a Role from a user
    // -------------------------------------------

    def "removeRole: role is removed from the user"() {
        given:
        final roleAdmin = testDataService.roleAdmin();
        final userAdmin = testDataService.userAdmin()

        and:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(userAdmin.getId().toString())
                .idRole(roleAdmin.getId().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "removeRole: validation fails when missing idUser"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idRole(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "removeRole: validation fails when missing idRole"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "removeRole: validation fails when missing idUser and idRole"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder().build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "removeRole: validation fails when idUser is not an UUID"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser("not an UUID")
                .idRole(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_NOT_UUID.getCode())
    }

    def "removeRole: validation fails when idRole is not an UUID"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole("not an UUID")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_NOT_UUID.getCode())
    }

    def "removeRole: validation fails when neither idRole nor idUser is not an UUID"() {
        given:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser("not an UUID")
                .idRole("not an UUID")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_NOT_UUID.getCode(), ApiErrorMessage.STRING_NOT_UUID.getCode())
    }

    def "removeRole: validation fails when Role does not exist"() {
        given:
        final userPedrola = testDataService.userPedrola();

        and:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(userPedrola.getId().toString())
                .idRole(UUID.randomUUID().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.ROLE_NOT_FOUND.getCode())
    }

    def "removeRole: validation fails when User does not exist"() {
        given:
        final roleAdmin = testDataService.roleAdmin();

        and:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(UUID.randomUUID().toString())
                .idRole(roleAdmin.getId().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.USER_NOT_FOUND.getCode())
    }

    def "removeRole: validation fails when User does not contain the Role"() {
        given:
        final roleAdmin = testDataService.roleAdmin();
        final userPedrola = testDataService.userPedrola();

        and:
        final removeRoleDTO = new RemoveRoleDTO.Builder()
                .idUser(userPedrola.getId().toString())
                .idRole(roleAdmin.getId().toString())
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_USER_CONTROLLER + ROUTING_USER_ROLE)
                .content(objectMapper.writeValueAsString(removeRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.USER_DOES_NOT_HAVE_THE_ROLE.getCode())
    }

}
