package com.kiesoft.userapi.controller.role;

import com.kiesoft.userapi.dto.user.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.kiesoft.userapi.controller.role.AbstractRoleController.ROUTING_ROLE_CONTROLLER;

@RestController
@RequestMapping(ROUTING_ROLE_CONTROLLER)
public class RoleController extends AbstractRoleController {

    @RequestMapping(value = ROUTING_ROLE_CREATE, method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createNewRole() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
