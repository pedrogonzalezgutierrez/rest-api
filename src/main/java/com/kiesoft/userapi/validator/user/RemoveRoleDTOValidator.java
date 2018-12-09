package com.kiesoft.userapi.validator.user;

import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.dto.user.RemoveRoleDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.role.RoleService;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class RemoveRoleDTOValidator implements Validator {

    private final ValidatorHelper validatorHelper;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public RemoveRoleDTOValidator(ValidatorHelper validatorHelper, RoleService roleService, UserService userService) {
        this.validatorHelper = validatorHelper;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RemoveRoleDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RemoveRoleDTO removeRoleDTO = (RemoveRoleDTO) target;

        // idUser and idRole cannot by blank and they need to be a valid UUID
        validatorHelper.rejectStringIfNotUUID(
                "idUser",
                removeRoleDTO.getIdUser(),
                errors);

        validatorHelper.rejectStringIfNotUUID(
                "idRole",
                removeRoleDTO.getIdRole(),
                errors);

        if (errors.hasErrors()) {
            return;
        }

        Optional<RoleDTO> roleDTO = roleService.findById(UUID.fromString(removeRoleDTO.getIdRole()));
        if (roleDTO.isPresent()) {
            Optional<UserDTO> userDTO = userService.findById(UUID.fromString(removeRoleDTO.getIdUser()));

            if(userDTO.isPresent()) {
                // Check the User has the Role
                if(userDTO.get().getRoles().contains(roleDTO.get())) {
                    // Populate the fields for the controller
                    removeRoleDTO.setRoleDTO(roleDTO.get());
                    removeRoleDTO.setUserDTO(userDTO.get());
                } else {
                    // User does not have role
                    errors.rejectValue("idUser", ApiErrorMessage.USER_DOES_NOT_HAVE_THE_ROLE.getCode(), ApiErrorMessage.USER_DOES_NOT_HAVE_THE_ROLE.getMessage());
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
