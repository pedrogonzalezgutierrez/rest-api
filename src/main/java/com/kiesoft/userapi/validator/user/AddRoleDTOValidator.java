package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.dto.user.AddRoleDTO;
import com.kiesoft.userapi.dto.user.CreateUserDTO;
import com.kiesoft.userapi.service.role.RoleService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

import static com.kiesoft.userapi.validator.role.RoleProperties.NAME_LENGTH_MAX;
import static com.kiesoft.userapi.validator.role.RoleProperties.NAME_LENGTH_MIN;

@Component
public class AddRoleDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;
    private final RoleService roleService;

    @Autowired
    public AddRoleDTOValidator(final ValidatorHelper validatorHelper, final Environment env, final RoleService roleService) {
        this.validatorHelper = validatorHelper;
        this.env = env;
        this.roleService = roleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AddRoleDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddRoleDTO addRoleDTO = (AddRoleDTO) target;

        // name cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "name",
                addRoleDTO.getName(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // name maximum and minimum length
        validatorHelper.rejectStringIfNotInLength(
                "name",
                addRoleDTO.getName(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(NAME_LENGTH_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(NAME_LENGTH_MAX))),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // Check role does not exist
        Optional<RoleDTO> roleDTO = roleService.findByName(addRoleDTO.getName());
        if (!roleDTO.isPresent()) {
            errors.rejectValue("name", ApiErrorMessage.ROLE_NOT_FOUND.getCode(), ApiErrorMessage.ROLE_NOT_FOUND.getMessage());
        }

    }
}
