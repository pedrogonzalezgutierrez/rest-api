package com.kiesoft.customer.controller.user;

import com.kiesoft.customer.dto.user.CreateUserDTO;
import com.kiesoft.customer.mapper.user.UserMapper;
import com.kiesoft.customer.dto.user.UserDTO;
import com.kiesoft.customer.jpa.entity.user.UserEntity;
import com.kiesoft.customer.service.user.UserService;
import com.kiesoft.customer.validator.user.CreateUserDTOValidator;
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
import java.util.UUID;

import static com.kiesoft.customer.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER;

@RestController
@RequestMapping(ROUTING_USER_CONTROLLER)
public class UserController extends AbstractUserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final CreateUserDTOValidator createUserDTOValidator;

    @Autowired
    public UserController(final UserService userService, final UserMapper userMapper, final CreateUserDTOValidator createUserDTOValidator) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.createUserDTOValidator = createUserDTOValidator;
    }

    @InitBinder("createUserDTO")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(createUserDTOValidator);
    }

    @RequestMapping(value = ROUTING_CREATE, method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        UserDTO userDTO = new UserDTO.Builder().id(UUID.randomUUID()).build();

        UserEntity userEntity = userMapper.asEntity(userDTO);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
