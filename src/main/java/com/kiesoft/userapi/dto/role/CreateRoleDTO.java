package com.kiesoft.userapi.dto.role;

public class CreateRoleDTO {

    private String name;

    public String getName() {
        return name;
    }

    public static final class Builder {
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public CreateRoleDTO build() {
            CreateRoleDTO createRoleDTO = new CreateRoleDTO();
            createRoleDTO.name = this.name;
            return createRoleDTO;
        }
    }

}
