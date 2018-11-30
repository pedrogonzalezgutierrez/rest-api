package com.kiesoft.customer.error;

public enum ApiErrorMessage {

    FIELD_REQUIRED("field-required", "This field is required"),
    STRING_BLANK("string-blank", "This field can not be null of empty"),
    STRING_LENGTH("string-length", "The length of this string has to be between {0} and {1} characters"),
    INTEGER_RANGE("integer-range", "This number has to be less than {0} and greater than {1}");


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
