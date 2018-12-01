package com.kiesoft.customer.mapper.role;

import com.kiesoft.customer.dto.role.RoleDTO;
import com.kiesoft.customer.jpa.entity.role.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleConverter {

    RoleDTO asDTO(RoleEntity roleEntity);

    RoleEntity asEntity(RoleDTO roleDTO);

}
