package com.kiesoft.customer.service.user;

import com.kiesoft.customer.dto.user.UserDTO;
import com.kiesoft.customer.jpa.entity.user.UserEntity;
import com.kiesoft.customer.service.SaveEntityService;

import java.util.Optional;

public interface UserService extends SaveEntityService<UserDTO> {

    Optional<UserDTO> findByName(String name);

}
