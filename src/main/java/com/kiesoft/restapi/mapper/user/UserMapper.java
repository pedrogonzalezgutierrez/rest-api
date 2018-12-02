package com.kiesoft.restapi.mapper.user;

import com.kiesoft.restapi.dto.user.UserDTO;
import com.kiesoft.restapi.jpa.entity.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO asDTO(UserEntity userEntity);

    UserEntity asEntity(UserDTO userDTO);

}
