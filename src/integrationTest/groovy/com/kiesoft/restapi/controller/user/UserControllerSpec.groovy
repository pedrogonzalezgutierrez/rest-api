package com.kiesoft.restapi.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.kiesoft.customer.controller.error.ApiValidationExceptionHandler
import com.kiesoft.customer.dto.user.CreateUserDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.kiesoft.customer.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "integrationTest")
/*
@SqlGroup({
    @Sql(scripts = "script1.sql", config = @SqlConfig(dataSource = "dataSource1", transactionManager = "txMgr1")),
    @Sql(scripts = "script2.sql", config = @SqlConfig(dataSource = "dataSource2", transactionManager = "txMgr2"))
})

@SqlGroup(value = {
    @Sql(scripts = "script1.sql")
    }
)

@SqlGroup({
    @Sql(
            scripts = "/com/kiesoft/sstarter/common-data.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    ),

    @Sql(
            scripts = "/com/kiesoft/sstarter/clean-db.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
})
*/
//@Transactional

@SqlGroup([
    @Sql(
            scripts = "/com/kiesoft/sstarter/common-data.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = BEFORE_TEST_METHOD
    ),

    @Sql(
            scripts = "/com/kiesoft/sstarter/clean-db.sql",
            config = @SqlConfig(transactionMode = ISOLATED),
            executionPhase = AFTER_TEST_METHOD
    )
])
class UserControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    UserController userController

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

    def "user2 created when it does not exist and validation successful"() {
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


}
