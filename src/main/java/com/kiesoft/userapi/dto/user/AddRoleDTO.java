package com.kiesoft.userapi.dto.user;

public class AddRoleDTO {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class Builder {
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public AddRoleDTO build() {
            AddRoleDTO addRoleDTO = new AddRoleDTO();
            addRoleDTO.setName(name);
            return addRoleDTO;
        }
    }

}
