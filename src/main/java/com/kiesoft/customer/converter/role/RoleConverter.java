package com.kiesoft.customer.converter.role;

import com.kiesoft.customer.dto.role.RoleDTO;
import com.kiesoft.customer.jpa.entity.role.RoleEntity;
import fr.xebia.extras.selma.Mapper;

@Mapper
public interface RoleConverter {

    RoleDTO asDTO(RoleEntity roleEntity);

    RoleEntity asEntity(RoleDTO roleDTO);

}
