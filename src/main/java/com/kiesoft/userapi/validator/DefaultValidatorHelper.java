package com.kiesoft.userapi.validator;

import com.kiesoft.userapi.error.ApiErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Objects;

@Component
public class DefaultValidatorHelper implements ValidatorHelper {

    /**
     * Used to remove HTML and Javascript core from a string. This policy does not allow anything, just raw text
     */
    private final PolicyFactory sanitizer = new HtmlPolicyBuilder().toFactory();

    /**
     * Used to validate an email
     */
    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public void rejectIfStringIsBlank(String field, String string, Errors errors) {
        if (StringUtils.isBlank(string)) {
            errors.rejectValue(field, ApiErrorMessage.STRING_BLANK.getCode(), ApiErrorMessage.STRING_BLANK.getMessage());
        }
    }

    @Override
    public void rejectStringIfNotInLength(String field, String string, int min, int max, Errors errors) {
        rejectIfStringIsBlank(field, string, errors);
        if (!StringUtils.isBlank(string)) {
            if ((string.length() < min) || (string.length() > max)) {
                errors.rejectValue(field, ApiErrorMessage.STRING_LENGTH.getCode(), String.format(ApiErrorMessage.STRING_LENGTH.getMessage(), min, max));
            }
        }
    }

    @Override
    public void rejectIntegerIfNotInRange(String field, Integer number, int min, int max, Errors errors) {
        if (Objects.isNull(number)) {
            errors.rejectValue(field, ApiErrorMessage.FIELD_REQUIRED.getCode(), ApiErrorMessage.FIELD_REQUIRED.getMessage());
        } else {
            if ((number < min) || (number > max)) {
                errors.rejectValue(field, ApiErrorMessage.INTEGER_RANGE.getCode(), String.format(ApiErrorMessage.INTEGER_RANGE.getMessage(), min, max));
            }
        }
    }

    @Override
    public void rejectBooleanIfNull(String field, Boolean value, Errors errors) {
        if (Objects.isNull(value)) {
            errors.rejectValue(field, ApiErrorMessage.FIELD_REQUIRED.getCode(), ApiErrorMessage.FIELD_REQUIRED.getMessage());
        }
    }

    @Override
    public String removeHTMLandJS(String untrustedString) {
        return sanitizer.sanitize(untrustedString);
    }

    @Override
    public void rejectIfNotEmail(String field, String email, Errors errors) {
        if (!emailValidator.isValid(email)) {
            errors.rejectValue(field, ApiErrorMessage.EMAIL_INVALID.getCode(), ApiErrorMessage.EMAIL_INVALID.getMessage());
        }
    }

}
