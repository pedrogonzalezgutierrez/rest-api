package com.kiesoft.userapi.service.role;

import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.service.FindOneService;
import com.kiesoft.userapi.service.SaveEntityService;

import java.util.Optional;

public interface RoleService extends SaveEntityService<RoleDTO>, FindOneService<RoleDTO> {

    Optional<RoleDTO> findByName(String name);

}
