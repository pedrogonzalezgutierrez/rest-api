package com.kiesoft.restapi.controller.user;

import com.kiesoft.restapi.dto.user.CreateUserDTO;
import com.kiesoft.restapi.dto.user.UserDTO;
import com.kiesoft.restapi.service.user.UserService;
import com.kiesoft.restapi.validator.user.CreateUserDTOValidator;
import org.apache.commons.codec.digest.DigestUtils;
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

import static com.kiesoft.restapi.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER;

@RestController
@RequestMapping(ROUTING_USER_CONTROLLER)
public class UserController extends AbstractUserController {

    private final UserService userService;
    private final CreateUserDTOValidator createUserDTOValidator;

    @Autowired
    public UserController(final UserService userService, final CreateUserDTOValidator createUserDTOValidator) {
        this.userService = userService;
        this.createUserDTOValidator = createUserDTOValidator;
    }

    @InitBinder("createUserDTO")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(createUserDTOValidator);
    }

    @RequestMapping(value = ROUTING_CREATE, method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        userService.save(new UserDTO.Builder()
                .name(createUserDTO.getName())
                .password(DigestUtils.md5Hex(createUserDTO.getPassword()))
                .enabled(Boolean.TRUE)
                .points(0)
                .build());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
