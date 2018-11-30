package com.kiesoft.customer.validator;

import com.kiesoft.customer.error.ApiErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Objects;

@Component
public class DefaultValidatorHelper implements ValidatorHelper {

    @Override
    public Boolean rejectIfStringIsBlank(String field, String string, Errors errors) {
        if (StringUtils.isBlank(string)) {
            errors.rejectValue(field, ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getMessage());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean rejectStringIfNotInLength(String field, String string, int min, int max, Errors errors) {
        if (!rejectIfStringIsBlank(field, string, errors)) {
            if ((string.length() < min) || (string.length() > max)) {
                errors.rejectValue(field, ApiErrorMessage.STRING_LENGTH.getCode(), new Object[]{min, max}, ApiErrorMessage.STRING_LENGTH.getMessage());
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean rejectIntegerIfNotInRange(String field, Integer number, int min, int max, Errors errors) {
        if (Objects.nonNull(number)) {
            if ((number < min) || (number > max)) {
                errors.rejectValue(field, ApiErrorMessage.INTEGER_RANGE.getCode(), new Object[]{min, max}, ApiErrorMessage.INTEGER_RANGE.getMessage());
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public String sanitizeString(String untrustedString) {
        // I dont allow anything, just raw text
        PolicyFactory sanitizer = new HtmlPolicyBuilder().toFactory();
        return sanitizer.sanitize(untrustedString);
    }

}
