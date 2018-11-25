package com.gvc.customer.validator;

import org.springframework.validation.Errors;

public interface ValidatorHelper {

    Boolean rejectIfStringIsBlank(String field, String string, Errors errors);
    Boolean rejectStringIfNotInLength(String field, String string, int min, int max, Errors errors);
    Boolean rejectIntegerIfNotInRange(String field, Integer number, int min, int max, Errors errors);
    String sanitizeString(String untrustedString);
}
