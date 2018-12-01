package com.kiesoft.customer.converter.user;

import com.kiesoft.customer.dto.user.UserDTO;
import com.kiesoft.customer.jpa.entity.user.UserEntity;
import fr.xebia.extras.selma.Mapper;

@Mapper
public interface UserConverter {

    UserDTO asDTO(UserEntity userEntity);

    UserEntity asEntity(UserDTO userDTO);

}
