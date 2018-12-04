package com.kiesoft.userapi.controller.error;

public class ApiFieldError {
    private String field;
    private String code;
    private String message;

    public ApiFieldError() {
    }

    public ApiFieldError(String field, String code, String message) {
        this.field = field;
        this.code = code;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
