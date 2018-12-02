package com.kiesoft.customer.validator;

import org.springframework.validation.Errors;

public interface ValidatorHelper {

    void rejectIfStringIsBlank(String field, String string, Errors errors);
    void rejectStringIfNotInLength(String field, String string, int min, int max, Errors errors);
    void rejectIntegerIfNotInRange(String field, Integer number, int min, int max, Errors errors);
    String removeHTMLandJS(String untrustedString);
}
