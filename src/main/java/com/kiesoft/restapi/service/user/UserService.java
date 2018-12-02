package com.kiesoft.restapi.service.user;

import com.kiesoft.restapi.dto.user.UserDTO;
import com.kiesoft.restapi.service.SaveEntityService;

import java.util.Optional;

public interface UserService extends SaveEntityService<UserDTO> {

    Optional<UserDTO> findByName(String name);

}
