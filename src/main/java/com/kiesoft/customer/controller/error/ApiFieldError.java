package com.kiesoft.customer.controller.error;

public class ApiFieldError {
    private String field;
    private String code;

    public ApiFieldError(String field, String code) {
        this.field = field;
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

}
