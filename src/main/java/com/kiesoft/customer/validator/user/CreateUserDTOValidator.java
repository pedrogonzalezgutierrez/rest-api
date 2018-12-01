package com.kiesoft.customer.validator.user;

import com.kiesoft.customer.dto.user.CreateUserDTO;
import com.kiesoft.customer.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class CreateUserDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;

    @Autowired
    public CreateUserDTOValidator(final ValidatorHelper validatorHelper, final Environment env) {
        this.validatorHelper = validatorHelper;
        this.env = env;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateUserDTO createUserDTO = (CreateUserDTO) target;
        createUserDTO.setName(validatorHelper.sanitizeString(createUserDTO.getName()));

        validatorHelper.rejectIfStringIsBlank(
                "name",
                createUserDTO.getName(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "password",
                createUserDTO.getPassword(),
                errors);

        if(errors.hasErrors()) {
            return;
        }

        validatorHelper.rejectStringIfNotInLength(
                "user",
                createUserDTO.getName(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty("user.name.length.min"))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty("user.name.length.max"))),
                errors);

        validatorHelper.rejectStringIfNotInLength(
                "password",
                createUserDTO.getPassword(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty("user.password.length.min"))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty("user.password.length.max"))),
                errors);
    }

}
