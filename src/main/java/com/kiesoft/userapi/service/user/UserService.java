package com.kiesoft.userapi.service.user;

import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.service.SaveEntityService;

import java.util.Optional;

public interface UserService extends SaveEntityService<UserDTO> {

    Optional<UserDTO> findByName(String name);
    Optional<UserDTO> findByEmail(String email);
    Optional<UserDTO> findByEmailAndPassword(String email, String password);

}
