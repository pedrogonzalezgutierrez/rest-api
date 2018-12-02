package com.kiesoft.userapi.dto.role;

import com.kiesoft.userapi.domain.role.Role;
import com.kiesoft.userapi.dto.AbstractDTO;

import java.util.UUID;

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


    public static final class Builder {
        private UUID id;
        private String name;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public RoleDTO build() {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(id);
            roleDTO.setName(name);
            return roleDTO;
        }
    }

}
