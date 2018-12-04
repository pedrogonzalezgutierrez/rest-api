package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.dto.user.EnableUserDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.error.ApiErrorMessage;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class EnableUserDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final UserService userService;

    @Autowired
    public EnableUserDTOValidator(final ValidatorHelper validatorHelper, final UserService userService) {
        this.validatorHelper = validatorHelper;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EnableUserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EnableUserDTO enableUserDTO = (EnableUserDTO) target;

        // email and enable cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "email",
                enableUserDTO.getEmail(),
                errors);

        validatorHelper.rejectBooleanIfNull(
                "enable",
                enableUserDTO.getEnable(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // valid email
        validatorHelper.rejectIfNotEmail(
                "email",
                enableUserDTO.getEmail(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // Search the user by email
        Optional<UserDTO> userDTO = userService.findByEmail(enableUserDTO.getEmail());
        if (userDTO.isPresent()) {

            // Check enable is not the same
            if (!Objects.equals(userDTO.get().getEnabled(), enableUserDTO.getEnable())) {
                // Populate the field for the controller
                enableUserDTO.setUserDTO(userDTO.get());

            } else {
                errors.rejectValue("enable", ApiErrorMessage.FIELD_NOT_CHANGED.getCode(), ApiErrorMessage.FIELD_NOT_CHANGED.getMessage());
            }

        } else {
            errors.rejectValue("email", ApiErrorMessage.EMAIL_NOT_FOUND.getCode(), ApiErrorMessage.EMAIL_NOT_FOUND.getMessage());
        }

    }
}
