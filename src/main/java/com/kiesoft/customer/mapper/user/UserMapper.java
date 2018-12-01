package com.kiesoft.customer.mapper.user;

import com.kiesoft.customer.dto.user.UserDTO;
import com.kiesoft.customer.jpa.entity.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO asDTO(UserEntity userEntity);

    UserEntity asEntity(UserDTO userDTO);

}
