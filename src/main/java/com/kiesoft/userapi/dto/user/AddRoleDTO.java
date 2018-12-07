package com.kiesoft.userapi.dto.user;

import com.kiesoft.userapi.dto.role.RoleDTO;

import java.util.UUID;

public class AddRoleDTO {

    private String idUser;
    private String idRole;
    private RoleDTO roleDTO;
    private UserDTO userDTO;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdRole() {
        return idRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public RoleDTO getRoleDTO() {
        return roleDTO;
    }

    public void setRoleDTO(RoleDTO roleDTO) {
        this.roleDTO = roleDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public static final class Builder {
        private String idUser;
        private String idRole;

        public Builder idUser(String idUser) {
            this.idUser = idUser;
            return this;
        }

        public Builder idRole(String idRole) {
            this.idRole = idRole;
            return this;
        }

        public AddRoleDTO build() {
            AddRoleDTO addRoleDTO = new AddRoleDTO();
            addRoleDTO.setIdUser(idUser);
            addRoleDTO.setIdRole(idRole);
            return addRoleDTO;
        }
    }

}
