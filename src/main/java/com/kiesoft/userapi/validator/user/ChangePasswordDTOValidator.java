package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.dto.user.ChangePasswordDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.error.ApiErrorMessage;
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

import static com.kiesoft.userapi.validator.user.CreateUserDTOValidator.USER_PASSWORD_MAX;
import static com.kiesoft.userapi.validator.user.CreateUserDTOValidator.USER_PASSWORD_MIN;

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
        ChangePasswordDTO generateJwtDTO = (ChangePasswordDTO) target;

        // email and password cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "email",
                generateJwtDTO.getEmail(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "password",
                generateJwtDTO.getPassword(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "newPassword",
                generateJwtDTO.getNewPassword(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // name and password maximum and minimum length
        validatorHelper.rejectStringIfNotInLength(
                "password",
                generateJwtDTO.getPassword(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_PASSWORD_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_PASSWORD_MAX))),
                errors);
        validatorHelper.rejectStringIfNotInLength(
                "newPassword",
                generateJwtDTO.getNewPassword(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_PASSWORD_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(USER_PASSWORD_MAX))),
                errors);

        // valid email
        validatorHelper.rejectIfNotEmail(
                "email",
                generateJwtDTO.getEmail(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        Optional<UserDTO> userDTO = userService.findByEmailAndPassword(generateJwtDTO.getEmail(), DigestUtils.md5Hex(generateJwtDTO.getPassword()));
        if (userDTO.isPresent()) {
            if (!userDTO.get().getEnabled()) {
                errors.rejectValue("email", ApiErrorMessage.USERNAME_NOT_ENABLED.getCode(), ApiErrorMessage.USERNAME_NOT_ENABLED.getMessage());
            }
        } else {
            errors.rejectValue("email", ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getMessage());
            errors.rejectValue("password", ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getMessage());
        }

    }
}
