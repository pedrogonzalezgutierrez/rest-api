package com.kiesoft.userapi.validator.user

import com.kiesoft.userapi.dto.role.RoleDTO
import com.kiesoft.userapi.dto.user.AddRoleDTO
import com.kiesoft.userapi.service.role.RoleService
import com.kiesoft.userapi.validator.DefaultValidatorHelper
import org.springframework.core.env.Environment
import spock.lang.Specification

class AddRoleDTOValidatorSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()
    final env = Mock(Environment)
    final roleService = Mock(RoleService)
    final addRoleDTOValidator = new AddRoleDTOValidator(validatorHelper, env, roleService)

    final addRoleDTO = new AddRoleDTO.Builder()
            .id(UUID.randomUUID())
            .name("ROLE_ADMIN")
            .build()


}
