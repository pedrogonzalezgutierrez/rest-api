package com.kiesoft.customer.validator.customer;

import com.kiesoft.customer.dto.customer.CustomerDTO;
import com.kiesoft.customer.error.ApiErrorMessage;
import com.kiesoft.customer.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CustomerDTOAliasesValidator implements Validator {

    private final ValidatorHelper validatorHelper;

    @Autowired
    public CustomerDTOAliasesValidator(ValidatorHelper validatorHelper) {
        this.validatorHelper = validatorHelper;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerDTO customerDTO = (CustomerDTO) target;

        if (customerDTO.getAliases().isEmpty()) {
            errors.rejectValue("aliases", ApiErrorMessage.FIELD_REQUIRED.getCode(), ApiErrorMessage.FIELD_REQUIRED.getMessage());
            return;
        }
        customerDTO.getAliases().forEach(aliasDTO -> validatorHelper.rejectIfStringIsBlank("aliases.name", aliasDTO.getName(), errors));
    }

}
