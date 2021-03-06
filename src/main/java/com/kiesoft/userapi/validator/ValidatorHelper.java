package com.kiesoft.userapi.validator;

import org.springframework.validation.Errors;

public interface ValidatorHelper {

    void rejectIfStringIsBlank(String field, String string, Errors errors);
    void rejectStringIfNotInLength(String field, String string, int min, int max, Errors errors);
    void rejectStringIfNotUUID(String field, String string, Errors errors);
    void rejectIntegerIfNotInRange(String field, Integer number, int min, int max, Errors errors);
    void rejectBooleanIfNull(String field, Boolean value, Errors errors);
    String removeHTMLandJS(String untrustedString);
    void rejectIfNotEmail(String field, String email, Errors errors);
}
