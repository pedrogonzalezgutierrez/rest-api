package com.kiesoft.restapi.error;

public enum ApiErrorMessage {

    FIELD_REQUIRED("field-required", "This field is required"),

    STRING_BLANK("string-blank", "This field can not be null, empty or whitespaces"),
    STRING_LENGTH("string-length", "The length of this string has to be between %d and %d characters"),

    INTEGER_RANGE("integer-range", "This number has to be less than %d and greater than %d"),

    USERNAME_ALREADY_EXISTS("username-already-exists", "The username is in use by other user");


    private String code;
    private String message;

    ApiErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
