package com.kiesoft.userapi.dto.role;

public class DeleteRoleDTO {

    private String name;
    private RoleDTO roleDTO;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleDTO getRoleDTO() {
        return roleDTO;
    }

    public void setRoleDTO(RoleDTO roleDTO) {
        this.roleDTO = roleDTO;
    }

    public static final class Builder {
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public DeleteRoleDTO build() {
            DeleteRoleDTO deleteRoleDTO = new DeleteRoleDTO();
            deleteRoleDTO.setName(name);
            return deleteRoleDTO;
        }
    }

}
