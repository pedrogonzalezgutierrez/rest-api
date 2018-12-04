package com.kiesoft.userapi.controller.role;

import com.kiesoft.userapi.dto.role.CreateRoleDTO;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.role.RoleService;
import com.kiesoft.userapi.validator.role.CreateRoleDTOValidator;
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

import static com.kiesoft.userapi.controller.role.AbstractRoleController.ROUTING_ROLE_CONTROLLER;

@RestController
@RequestMapping(ROUTING_ROLE_CONTROLLER)
public class RoleController extends AbstractRoleController {

    private final RoleService roleService;
    private final CreateRoleDTOValidator createRoleDTOValidator;

    @Autowired
    public RoleController(RoleService roleService, CreateRoleDTOValidator createRoleDTOValidator) {
        this.roleService = roleService;
        this.createRoleDTOValidator = createRoleDTOValidator;
    }

    @InitBinder("createRoleDTO")
    public void setupCreateNewRole(WebDataBinder binder) {
        binder.addValidators(createRoleDTOValidator);
    }

    @RequestMapping(value = ROUTING_ROLE_CREATE, method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createNewRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) {
        roleService.save(new RoleDTO.Builder()
                .name(createRoleDTO.getName())
                .build());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
