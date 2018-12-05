package com.kiesoft.userapi.controller.error

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.context.request.WebRequest
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.tuple

class ApiValidationExceptionHandlerSpec extends Specification {

    final apiValidationExceptionHandler = new ApiValidationExceptionHandler()

    final ex = Mock(MethodArgumentNotValidException)
    final headers = Mock(HttpHeaders)
    final request = Mock(WebRequest)
    final bindingResult = Mock(BindingResult)

    final objectMapper = new ObjectMapper();

    def "will return ApiErrorsView populated"() {
        given:
        final fieldError1 = new FieldError(
                "object1",
                "stringField",
                "SuColega",
                true,
                null,
                ["argument1", "argument2"] as String[],
                "String message")

        final fieldError2 = new FieldError(
                "object2",
                "integerField",
                "25",
                false,
                [] as String[],
                ["argument1", "argument2"] as String[],
                "Integer message")

        final fieldError3 = new FieldError(
                "object3",
                "booleanField",
                "false",
                true,
                ["booleanCode"] as String[],
                ["argument1", "argument2"] as String[],
                "Boolean message")

        final fieldError4 = new FieldError(
                "object4",
                "doubleField",
                "2.2D",
                false,
                ["doubleCode1", "doubleCode2"] as String[],
                ["argument1", "argument2"] as String[],
                "Double message")

        and:
        ex.getBindingResult() >> bindingResult

        and:
        bindingResult.getFieldErrors() >> [fieldError1, fieldError2, fieldError3, fieldError4]

        when:
        final actual = apiValidationExceptionHandler.handleMethodArgumentNotValid(ex, headers, HttpStatus.BAD_REQUEST, request)

        then:
        Objects.nonNull(actual)

        and:
        assertThat(((ApiErrorsView)actual.getBody()).fieldErrors)
                .extracting("field", "code", "message")
                .contains(
                tuple("stringField", ApiErrorMessage.UNKNOWN_CODE.getCode(), "String message"),
                tuple("integerField", ApiErrorMessage.UNKNOWN_CODE.getCode(), "Integer message"),
                tuple("booleanField", "booleanCode", "Boolean message"),
                tuple("doubleField", "doubleCode2", "Double message"))
    }
    
}
