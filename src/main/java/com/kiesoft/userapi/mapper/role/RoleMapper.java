package com.kiesoft.userapi.mapper.role;

import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.jpa.entity.role.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO asDTO(RoleEntity roleEntity);

    RoleEntity asEntity(RoleDTO roleDTO);

}
