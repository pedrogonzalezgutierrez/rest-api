package com.kiesoft.customer.dto.role;

import com.kiesoft.customer.domain.role.Role;
import com.kiesoft.customer.dto.AbstractDTO;

public class RoleDTO extends AbstractDTO implements Role {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
