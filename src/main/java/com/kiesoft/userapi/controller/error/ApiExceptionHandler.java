package com.kiesoft.userapi.controller.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Handles the validations error
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ApiFieldError> apiFieldErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> new ApiFieldError(
                        fieldError.getField(),
                        asString(fieldError.getCodes()),
                        fieldError.getDefaultMessage()))

                .collect(toList());
        return new ResponseEntity<>(new ApiErrorsView.Builder().fieldErrors(apiFieldErrors).build(), HttpStatus.BAD_REQUEST);
    }

    private String asString(String[] codes) {
        try {
            return codes[codes.length - 1];
        } catch (Exception e) {
            return ApiErrorMessage.UNKNOWN_CODE.getCode();
        }
    }

}
