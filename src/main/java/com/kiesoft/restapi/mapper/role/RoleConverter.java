package com.kiesoft.restapi.mapper.role;

import com.kiesoft.restapi.dto.role.RoleDTO;
import com.kiesoft.restapi.jpa.entity.role.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleConverter {

    RoleDTO asDTO(RoleEntity roleEntity);

    RoleEntity asEntity(RoleDTO roleDTO);

}
