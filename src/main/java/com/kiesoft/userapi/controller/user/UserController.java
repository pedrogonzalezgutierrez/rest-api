package com.kiesoft.userapi.controller.user;

import com.kiesoft.userapi.auth.filter.AuthorizationFilterJWT;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.dto.user.AddRoleDTO;
import com.kiesoft.userapi.dto.user.ChangePasswordDTO;
import com.kiesoft.userapi.dto.user.CreateUserDTO;
import com.kiesoft.userapi.dto.user.GenerateJwtDTO;
import com.kiesoft.userapi.dto.user.RemoveRoleDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.user.UserService;
import com.kiesoft.userapi.validator.user.AddRoleDTOValidator;
import com.kiesoft.userapi.validator.user.ChangePasswordDTOValidator;
import com.kiesoft.userapi.validator.user.CreateUserDTOValidator;
import com.kiesoft.userapi.dto.user.EnableUserDTO;
import com.kiesoft.userapi.validator.user.EnableUserDTOValidator;
import com.kiesoft.userapi.validator.user.GenerateJwtDTOValidator;
import com.kiesoft.userapi.validator.user.RemoveRoleDTOValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    private final ChangePasswordDTOValidator changePasswordDTOValidator;
    private final EnableUserDTOValidator enableUserDTOValidator;
    private final AddRoleDTOValidator addRoleDTOValidator;
    private final RemoveRoleDTOValidator removeRoleDTOValidator;

    @Autowired
    public UserController(final UserService userService,
                          final CreateUserDTOValidator createUserDTOValidator,
                          final GenerateJwtDTOValidator generateJwtDTOValidator,
                          final ChangePasswordDTOValidator changePasswordDTOValidator,
                          final EnableUserDTOValidator enableUserDTOValidator,
                          final AddRoleDTOValidator addRoleDTOValidator,
                          final RemoveRoleDTOValidator removeRoleDTOValidator) {
        this.userService = userService;
        this.createUserDTOValidator = createUserDTOValidator;
        this.generateJwtDTOValidator = generateJwtDTOValidator;
        this.changePasswordDTOValidator = changePasswordDTOValidator;
        this.enableUserDTOValidator = enableUserDTOValidator;
        this.addRoleDTOValidator = addRoleDTOValidator;
        this.removeRoleDTOValidator = removeRoleDTOValidator;
    }

    @InitBinder("createUserDTO")
    public void setupCreateNewUser(WebDataBinder binder) {
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
    public void setupGenerateJWT(WebDataBinder binder) {
        binder.addValidators(generateJwtDTOValidator);
    }

    @RequestMapping(value = ROUTING_USER_JWT, method = RequestMethod.POST)
    public ResponseEntity<Void> generateJWT(@Valid @RequestBody GenerateJwtDTO generateJwtDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("%s %s", AuthorizationFilterJWT.BEARER_PREFIX, generateJwtDTO.getJwt()));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @InitBinder("changePasswordDTO")
    public void setupChangePassword(WebDataBinder binder) {
        binder.addValidators(changePasswordDTOValidator);
    }

    @RequestMapping(value = ROUTING_USER_UPDATE_PASSWORD, method = RequestMethod.PATCH)
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        UserDTO userDTO = changePasswordDTO.getUserDTO();
        userDTO.setPassword(DigestUtils.md5Hex(changePasswordDTO.getNewPassword()));
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @InitBinder("enableUserDTO")
    public void setupEnableUser(WebDataBinder binder) {
        binder.addValidators(enableUserDTOValidator);
    }

    @RequestMapping(value = ROUTING_USER_ENABLE_USER, method = RequestMethod.PATCH)
    public ResponseEntity<Void> enableUser(@Valid @RequestBody EnableUserDTO enableUserDTO) {
        UserDTO userDTO = enableUserDTO.getUserDTO();
        userDTO.setEnabled(enableUserDTO.getEnable());
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @InitBinder("addRoleDTO")
    public void setupAddRole(WebDataBinder binder) {
        binder.addValidators(addRoleDTOValidator);
    }

    @RequestMapping(value = ROUTING_USER_ROLE, method = RequestMethod.POST)
    public ResponseEntity<Void> addRole(@Valid @RequestBody AddRoleDTO addRoleDTO) {
        UserDTO userDTO = addRoleDTO.getUserDTO();
        RoleDTO roleDTO = addRoleDTO.getRoleDTO();
        userDTO.addRole(roleDTO);
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @InitBinder("removeRoleDTO")
    public void setupRemoveRoleDTO(WebDataBinder binder) {
        binder.addValidators(removeRoleDTOValidator);
    }

    @RequestMapping(value = ROUTING_USER_ROLE, method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeRole(@Valid @RequestBody RemoveRoleDTO removeRoleDTO) {
        UserDTO userDTO = removeRoleDTO.getUserDTO();
        RoleDTO roleDTO = removeRoleDTO.getRoleDTO();
        userDTO.removeRole(roleDTO);
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
