package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.dto.user.ChangePasswordDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

import static com.kiesoft.userapi.validator.user.UserProperties.PASSWORD_LENGTH_MAX;
import static com.kiesoft.userapi.validator.user.UserProperties.PASSWORD_LENGTH_MIN;

@Component
public class ChangePasswordDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;
    private final UserService userService;

    @Autowired
    public ChangePasswordDTOValidator(final ValidatorHelper validatorHelper, final Environment env, final UserService userService) {
        this.validatorHelper = validatorHelper;
        this.env = env;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordDTO changePasswordDTO = (ChangePasswordDTO) target;

        // email, password and newPassword cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "email",
                changePasswordDTO.getEmail(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "password",
                changePasswordDTO.getPassword(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "newPassword",
                changePasswordDTO.getNewPassword(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // password and newPassword maximum and minimum length
        validatorHelper.rejectStringIfNotInLength(
                "password",
                changePasswordDTO.getPassword(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(PASSWORD_LENGTH_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(PASSWORD_LENGTH_MAX))),
                errors);
        validatorHelper.rejectStringIfNotInLength(
                "newPassword",
                changePasswordDTO.getNewPassword(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(PASSWORD_LENGTH_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(PASSWORD_LENGTH_MAX))),
                errors);

        // password and newPassword needs to be different
        if (Objects.equals(changePasswordDTO.getPassword(), changePasswordDTO.getNewPassword())) {
            errors.rejectValue("email", ApiErrorMessage.PASSWORDS_ARE_EQUALS.getCode(), ApiErrorMessage.PASSWORDS_ARE_EQUALS.getMessage());
        }

        // valid email
        validatorHelper.rejectIfNotEmail(
                "email",
                changePasswordDTO.getEmail(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        Optional<UserDTO> userDTO = userService.findByEmailAndPassword(changePasswordDTO.getEmail(), DigestUtils.md5Hex(changePasswordDTO.getPassword()));
        if (userDTO.isPresent()) {
            if (!userDTO.get().getEnabled()) {
                errors.rejectValue("email", ApiErrorMessage.USER_NOT_ENABLED.getCode(), ApiErrorMessage.USER_NOT_ENABLED.getMessage());
            } else {
                // Populate the object for the Controller
                changePasswordDTO.setUserDTO(userDTO.get());
            }
        } else {
            errors.rejectValue("email", ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getMessage());
            errors.rejectValue("password", ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getMessage());
        }

    }
}
