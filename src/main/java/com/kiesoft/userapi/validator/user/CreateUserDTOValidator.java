package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.dto.user.CreateUserDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.error.ApiErrorMessage;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class CreateUserDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;
    private final UserService userService;

    final static String USER_LENGTH_MIN = "user.name.length.min";
    final static String USER_LENGTH_MAX = "user.name.length.max";
    final static String USER_PASSWORD_MIN = "user.password.length.min";
    final static String USER_PASSWORD_MAX = "user.password.length.max";

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

        // Remove rubbish from the name
        createUserDTO.setName(validatorHelper.removeHTMLandJS(createUserDTO.getName()));

        // name and password cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "name",
                createUserDTO.getName(),
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
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_LENGTH_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_LENGTH_MAX))),
                errors);

        validatorHelper.rejectStringIfNotInLength(
                "password",
                createUserDTO.getPassword(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_PASSWORD_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_PASSWORD_MAX))),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // Check the name is not taken
        Optional<UserDTO> userDTO = userService.findByName(createUserDTO.getName());
        if(userDTO.isPresent()) {
            errors.rejectValue("name", ApiErrorMessage.USERNAME_ALREADY_EXISTS.getCode(), ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage());
        }

    }

}
