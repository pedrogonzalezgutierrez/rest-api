package com.kiesoft.userapi.validator.role;

import com.kiesoft.userapi.dto.role.CreateRoleDTO;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.service.role.RoleService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

import static com.kiesoft.userapi.validator.role.RoleProperties.ROLE_LENGTH_MAX;
import static com.kiesoft.userapi.validator.role.RoleProperties.ROLE_LENGTH_MIN;

@Component
public class CreateRoleDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;
    private final RoleService roleService;

    @Autowired
    public CreateRoleDTOValidator(final ValidatorHelper validatorHelper, final Environment env, final RoleService roleService) {
        this.validatorHelper = validatorHelper;
        this.env = env;
        this.roleService = roleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateRoleDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateRoleDTO createRoleDTO = (CreateRoleDTO) target;

        // name cannot by blank
        validatorHelper.rejectIfStringIsBlank(
                "name",
                createRoleDTO.getName(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // name maximum and minimum length
        validatorHelper.rejectStringIfNotInLength(
                "name",
                createRoleDTO.getName(),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(ROLE_LENGTH_MIN))),
                Integer.valueOf(Objects.requireNonNull(env.getProperty(ROLE_LENGTH_MAX))),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        // Check role does not exist
        Optional<RoleDTO> roleDTO = roleService.findByName(createRoleDTO.getName());
        if (roleDTO.isPresent()) {
            errors.rejectValue("name", ApiErrorMessage.ROLE_ALREADY_EXISTS.getCode(), ApiErrorMessage.ROLE_ALREADY_EXISTS.getMessage());
        }
    }

}
