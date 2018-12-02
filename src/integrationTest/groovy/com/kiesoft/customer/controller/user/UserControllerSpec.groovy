package com.kiesoft.customer.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    void setup() throw1s Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(auditLogDataRestApi, new ApiValidationExceptionHandler()).build()
    }

}
