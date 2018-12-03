package com.kiesoft.userapi.controller.user;

import com.kiesoft.userapi.dto.user.CreateUserDTO;
import com.kiesoft.userapi.dto.user.GenerateJwtDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.user.CreateUserDTOValidator;
import com.kiesoft.userapi.validator.user.GenerateJwtDTOValidator;
import com.kiesoft.userapi.validator.user.JwtDTO;
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

import static com.kiesoft.userapi.controller.user.AbstractUserController.ROUTING_USER_CONTROLLER;

@RestController
@RequestMapping(ROUTING_USER_CONTROLLER)
public class UserController extends AbstractUserController {

    private final UserService userService;
    private final CreateUserDTOValidator createUserDTOValidator;
    private final GenerateJwtDTOValidator generateJwtDTOValidator;

    @Autowired
    public UserController(final UserService userService,
                          final CreateUserDTOValidator createUserDTOValidator,
                          final GenerateJwtDTOValidator generateJwtDTOValidator) {
        this.userService = userService;
        this.createUserDTOValidator = createUserDTOValidator;
        this.generateJwtDTOValidator = generateJwtDTOValidator;
    }

    @InitBinder("createUserDTO")
    public void setupcreateNewUser(WebDataBinder binder) {
        binder.addValidators(createUserDTOValidator);
    }

    @RequestMapping(value = ROUTING_USER_CREATE, method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        userService.save(new UserDTO.Builder()
                .name(createUserDTO.getName())
                .email(createUserDTO.getEmail())
                .password(DigestUtils.md5Hex(createUserDTO.getPassword()))
                .enabled(Boolean.TRUE)
                .points(0)
                .build());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @InitBinder("generateJwtDTO")
    public void setupRetrieveJWT(WebDataBinder binder) {
        binder.addValidators(generateJwtDTOValidator);
    }

    @RequestMapping(value = ROUTING_USER_JWT, method = RequestMethod.POST)
    public ResponseEntity<JwtDTO> retrieveJWT(@Valid @RequestBody GenerateJwtDTO generateJwtDTO) {
        final JwtDTO jwtDTO = new JwtDTO.Builder()
                .jwt(generateJwtDTO.getJwt())
                .build();
        return new ResponseEntity<>(jwtDTO, (HttpStatus.OK));
    }


}
