package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.dto.user.GenerateJwtDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.service.jwt.JwtService;
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

import static com.kiesoft.userapi.validator.user.UserProperties.USER_PASSWORD_MAX;
import static com.kiesoft.userapi.validator.user.UserProperties.USER_PASSWORD_MIN;

@Component
public class GenerateJwtDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public GenerateJwtDTOValidator(final ValidatorHelper validatorHelper,
                                   final Environment env,
                                   final UserService userService,
                                   final JwtService jwtService) {
        this.validatorHelper = validatorHelper;
        this.env = env;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GenerateJwtDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GenerateJwtDTO generateJwtDTO = (GenerateJwtDTO) target;

        // email and password cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "email",
                generateJwtDTO.getEmail(),
                errors);
        validatorHelper.rejectIfStringIsBlank(
                "password",
                generateJwtDTO.getPassword(),
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

        // valid email
        validatorHelper.rejectIfNotEmail(
                "email",
                generateJwtDTO.getEmail(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // Check credentials
        Optional<UserDTO> userDTO = userService.findByEmailAndPassword(generateJwtDTO.getEmail(), DigestUtils.md5Hex(generateJwtDTO.getPassword()));
        if (userDTO.isPresent()) {
            if(userDTO.get().getEnabled()) {
                // Populate the object (which will get the controller) with the jwt token
                Optional<String> jwt = jwtService.generateHS256(userDTO.get().getId(), userDTO.get().getPassword());
                if (jwt.isPresent()) {
                    generateJwtDTO.setJwt(jwt.get());
                } else {
                    errors.rejectValue("jwt", ApiErrorMessage.JWT_NOT_GENERATED.getCode(), ApiErrorMessage.JWT_NOT_GENERATED.getMessage());
                }
            } else {
                errors.rejectValue("email", ApiErrorMessage.USER_NOT_ENABLED.getCode(), ApiErrorMessage.USER_NOT_ENABLED.getMessage());
            }
        } else {
            errors.rejectValue("email", ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getMessage());
            errors.rejectValue("password", ApiErrorMessage.BAD_CREDENTIALS.getCode(), ApiErrorMessage.BAD_CREDENTIALS.getMessage());
        }

    }
}
