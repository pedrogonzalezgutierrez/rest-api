package com.kiesoft.userapi.controller.error;

public enum ApiErrorMessage {

    UNKNOWN_CODE("unknown-code", "Unknown error"),

    FIELD_REQUIRED("field-required", "This field is required"),
    FIELD_NOT_CHANGED("field-not-changed", "New value and old value are the same"),

    STRING_BLANK("string-blank", "This field can not be null, empty or whitespaces"),
    STRING_LENGTH("string-length", "The length of this string has to be between %d and %d characters"),
    STRING_NOT_UUID("string-not-uuid", "The string is not a valid UUID"),

    INTEGER_RANGE("integer-range", "This number has to be less than %d and greater than %d"),

    USERNAME_ALREADY_EXISTS("username-already-exists", "This username is in use by other user"),
    USER_NOT_ENABLED("user-not-enabled", "The user is not enabled"),
    USER_NOT_FOUND("user-not-found", "The user could not be found"),
    USER_NOT_SAVED("user-not-saved", "The user could not be saved"),
    USER_ALREADY_HAS_THE_ROLE("user-already-has-the-role", "The user already has that role"),
    USER_DOES_NOT_HAVE_THE_ROLE("user-does-not-have-the-role", "The user does not have that role"),

    EMAIL_INVALID("email-invalid", "This email is not valid"),
    EMAIL_ALREADY_EXISTS("email-already-exists", "This email is in use by other user"),
    EMAIL_NOT_FOUND("email-not-found", "This email is not associate to any user"),

    BAD_CREDENTIALS("bad-credentials", "Incorrect email or password"),
    JWT_NOT_GENERATED("jwt-not-generated", "JSON Web Token could not be generate. Please try again"),

    PASSWORDS_ARE_EQUALS("passwords-are-equals", "The new password needs to be different that the current one"),

    ROLE_ALREADY_EXISTS("role-already-exists", "This role already exists"),
    ROLE_NOT_FOUND("role-not-found", "The role could not be found"),
    ROLE_NOT_DELETED("role-not-deleted", "The role could not be deleted"),
    ROLE_NOT_SAVED("role-not-saved", "The role could not be saved");

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
