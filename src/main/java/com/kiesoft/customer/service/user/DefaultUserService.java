package com.kiesoft.customer.service.user;

import com.kiesoft.customer.dto.user.UserDTO;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class DefaultUserService implements UserService {

    @Override
    public UserDTO save(UserDTO userDTO) {
        return null;
    }

}
