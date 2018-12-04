package com.kiesoft.userapi.controller.error;

import java.util.List;

public class ApiErrorsView {
    private List<ApiFieldError> fieldErrors;

    public ApiErrorsView() {
    }

    public ApiErrorsView(List<ApiFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public List<ApiFieldError> getFieldErrors() {
        return fieldErrors;
    }
}
