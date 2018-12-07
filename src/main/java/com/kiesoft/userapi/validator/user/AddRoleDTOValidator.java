package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.dto.user.AddRoleDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.role.RoleService;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class AddRoleDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final Environment env;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AddRoleDTOValidator(final ValidatorHelper validatorHelper, final Environment env, final RoleService roleService, final UserService userService) {
        this.validatorHelper = validatorHelper;
        this.env = env;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AddRoleDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddRoleDTO addRoleDTO = (AddRoleDTO) target;

        // idUser and idRole cannot by blank
        validatorHelper.rejectStringIfNotUUID(
                "idUser",
                addRoleDTO.getIdUser(),
                errors);

        validatorHelper.rejectStringIfNotUUID(
                "idRole",
                addRoleDTO.getIdRole(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        Optional<RoleDTO> roleDTO = roleService.findById(UUID.fromString(addRoleDTO.getIdRole()));
        if (roleDTO.isPresent()) {
            Optional<UserDTO> userDTO = userService.findById(UUID.fromString(addRoleDTO.getIdUser()));

            if(userDTO.isPresent()) {
                // Check the User does not have the Role
                if(!userDTO.get().getRoles().contains(roleDTO.get())) {
                    // Populate the fields for the controller
                    addRoleDTO.setRoleDTO(roleDTO.get());
                    addRoleDTO.setUserDTO(userDTO.get());
                } else {
                    // User has got the role
                    errors.rejectValue("idUser", ApiErrorMessage.USER_ALREADY_HAS_THE_ROLE.getCode(), ApiErrorMessage.USER_ALREADY_HAS_THE_ROLE.getMessage());
                }

            } else {
                // User not found
                errors.rejectValue("idUser", ApiErrorMessage.USER_NOT_FOUND.getCode(), ApiErrorMessage.USER_NOT_FOUND.getMessage());
            }
        } else {
            // Role not found
            errors.rejectValue("idRole", ApiErrorMessage.ROLE_NOT_FOUND.getCode(), ApiErrorMessage.ROLE_NOT_FOUND.getMessage());
        }

    }
}
