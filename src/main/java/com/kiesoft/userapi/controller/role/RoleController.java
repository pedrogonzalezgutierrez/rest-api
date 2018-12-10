package com.kiesoft.userapi.controller.role;

import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.controller.error.ApiErrorsView;
import com.kiesoft.userapi.controller.error.ApiGlobalError;
import com.kiesoft.userapi.dto.role.CreateRoleDTO;
import com.kiesoft.userapi.dto.role.DeleteRoleDTO;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.role.RoleService;
import com.kiesoft.userapi.validator.role.CreateRoleDTOValidator;
import com.kiesoft.userapi.validator.role.DeleteRoleDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Collections;

import static com.kiesoft.userapi.controller.role.AbstractRoleController.ROUTING_ROLE_CONTROLLER;

@RestController
@RequestMapping(ROUTING_ROLE_CONTROLLER)
public class RoleController extends AbstractRoleController {

    private final RoleService roleService;
    private final CreateRoleDTOValidator createRoleDTOValidator;
    private final DeleteRoleDTOValidator deleteRoleDTOValidator;

    @Autowired
    public RoleController(final RoleService roleService, final CreateRoleDTOValidator createRoleDTOValidator, final DeleteRoleDTOValidator deleteRoleDTOValidator) {
        this.roleService = roleService;
        this.createRoleDTOValidator = createRoleDTOValidator;
        this.deleteRoleDTOValidator = deleteRoleDTOValidator;
    }

    @InitBinder("createRoleDTO")
    public void setupCreateRole(WebDataBinder binder) {
        binder.addValidators(createRoleDTOValidator);
    }

    @RequestMapping(value = ROUTING_MANAGE, method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) {
        roleService.save(new RoleDTO.Builder()
                .name(createRoleDTO.getName())
                .build());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @InitBinder("deleteRoleDTO")
    public void setupDeleteRole(WebDataBinder binder) {
        binder.addValidators(deleteRoleDTOValidator);
    }

    @RequestMapping(value = ROUTING_MANAGE, method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteRole(@Valid @RequestBody DeleteRoleDTO deleteRoleDTO) {
        try {
            RoleDTO roleDTO = deleteRoleDTO.getRoleDTO();
            roleService.delete(roleDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            ApiGlobalError apiGlobalError = new ApiGlobalError(ApiErrorMessage.ROLE_NOT_DELETED.getCode(), ApiErrorMessage.ROLE_NOT_DELETED.getMessage());
            return new ResponseEntity<>(new ApiErrorsView.Builder().globalErrors(Collections.singletonList(apiGlobalError)).build(), HttpStatus.BAD_REQUEST);
        }
    }

}
