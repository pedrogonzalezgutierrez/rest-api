package com.kiesoft.userapi.mapper.user;

import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.jpa.entity.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO asDTO(UserEntity userEntity);

    UserEntity asEntity(UserDTO userDTO);

}
