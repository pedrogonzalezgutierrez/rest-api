package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.dto.user.CreateUserDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

import static com.kiesoft.userapi.validator.user.UserProperties.NAME_LENGTH_MAX;
import static com.kiesoft.userapi.validator.user.UserProperties.NAME_LENGTH_MIN;
import static com.kiesoft.userapi.validator.user.UserProperties.PASSWORD_LENGTH_MAX;
import static com.kiesoft.userapi.validator.user.UserProperties.PASSWORD_LENGTH_MIN;

@Component
public class CreateUserDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;
    private final UserService userService;

    @Autowired
    public CreateUserDTOValidator(final ValidatorHelper validatorHelper, final Environment env, final UserService userService) {
        this.validatorHelper = validatorHelper;
        this.env = env;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateUserDTO createUserDTO = (CreateUserDTO) target;

        // Remove rubbish from input
        createUserDTO.setName(validatorHelper.removeHTMLandJS(createUserDTO.getName()));

        // name, email and password cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "name",
                createUserDTO.getName(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "email",
                createUserDTO.getEmail(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "password",
                createUserDTO.getPassword(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // name and password maximum and minimum length
        validatorHelper.rejectStringIfNotInLength(
                "name",
                createUserDTO.getName(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(NAME_LENGTH_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(NAME_LENGTH_MAX))),
                errors);

        validatorHelper.rejectStringIfNotInLength(
                "password",
                createUserDTO.getPassword(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(PASSWORD_LENGTH_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(PASSWORD_LENGTH_MAX))),
                errors);

        // valid email
        validatorHelper.rejectIfNotEmail(
                "email",
                createUserDTO.getEmail(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // Check the name is not taken
        Optional<UserDTO> userDTO = userService.findByName(createUserDTO.getName());
        if (userDTO.isPresent()) {
            errors.rejectValue("name", ApiErrorMessage.USERNAME_ALREADY_EXISTS.getCode(), ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage());
        }

        if (errors.hasErrors()) {
            return;
        }

        // Check the email is not taken
        Optional<UserDTO> userEmailDTO = userService.findByEmail(createUserDTO.getEmail());
        if (userEmailDTO.isPresent()) {
            errors.rejectValue("email", ApiErrorMessage.EMAIL_ALREADY_EXISTS.getCode(), ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

    }

}
