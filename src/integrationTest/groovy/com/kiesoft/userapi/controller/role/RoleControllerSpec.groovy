package com.kiesoft.userapi.controller.role

import com.fasterxml.jackson.databind.ObjectMapper
import com.kiesoft.userapi.TestDataService
import com.kiesoft.userapi.controller.error.ApiErrorMessage
import com.kiesoft.userapi.controller.error.ApiErrorsView
import com.kiesoft.userapi.controller.error.ApiExceptionHandler
import com.kiesoft.userapi.dto.role.DeleteRoleDTO
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

import static com.kiesoft.userapi.controller.role.AbstractRoleController.ROUTING_MANAGE
import static com.kiesoft.userapi.controller.role.AbstractRoleController.ROUTING_ROLE_CONTROLLER
import static org.assertj.core.api.Assertions.assertThat

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "integrationTest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoleControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    RoleController roleController

    @Autowired
    TestDataService testDataService

    final objectMapper = new ObjectMapper();

    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(roleController, new ApiExceptionHandler()).build()
    }

    //------------------------------
    // Create a Role
    //------------------------------

    def "createRole: role created when it does not exist and validation successful"() {
        given:
        final createRoleDTO = new CreateUserDTO.Builder()
                .name("ROLE_BETIS")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(createRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isCreated())
    }

    def "createRole: validation fails when missing name"() {
        given:
        final createRoleDTO = new CreateUserDTO.Builder().build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(createRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "createRole: validation fails when name too small"() {
        given:
        final createRoleDTO = new CreateUserDTO.Builder()
                .name("ROLE")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(createRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "createRole: validation fails when name too big"() {
        given:
        final createRoleDTO = new CreateUserDTO.Builder()
                .name("ROLE_REAL_BETIS_BALOMPIE")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(createRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "createRole: validation fails when role already exists"() {
        given:
        final createRoleDTO = new CreateUserDTO.Builder()
                .name(TestDataService.ROLE_ADMIN)
                .build()

        and:
        testDataService.usersAdminAndEditor()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.post(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(createRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.ROLE_ALREADY_EXISTS.getCode())
    }

    //------------------------------
    // Delete a Role
    //------------------------------

    def "deleteRole: role deleted when it does exist and validation successful"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name(TestDataService.ROLE_ADMIN)
                .build()

        and:
        testDataService.roleAdmin()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(deleteRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "deleteRole: role is not deleted when missing name"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder().build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(deleteRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_BLANK.getCode())
    }

    def "deleteRole: role is not deleted when name too small"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name("ROLE")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(deleteRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "deleteRole: role is not deleted when name too big"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name("ROLE_REAL_BETIS_BALOMPIE")
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(deleteRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.STRING_LENGTH.getCode())
    }

    def "deleteRole: role is not deleted when it does not exist"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name(TestDataService.ROLE_ADMIN)
                .build()

        when:
        final result = mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(deleteRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: "the error code is in the response"
        assertThat(objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ApiErrorsView.class).fieldErrors)
                .extracting("code")
                .contains(ApiErrorMessage.ROLE_NOT_FOUND.getCode())
    }

    def "deleteRole: role is not deleted when it contains users"() {
        given:
        final deleteRoleDTO = new DeleteRoleDTO.Builder()
                .name(TestDataService.ROLE_ADMIN)
                .build()

        and:
        testDataService.userAdmin()

        when:
        mockMvc.perform(MockMvcRequestBuilders.delete(ROUTING_ROLE_CONTROLLER + ROUTING_MANAGE)
                .content(objectMapper.writeValueAsString(deleteRoleDTO))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        thrown Exception
    }

}
