package com.kiesoft.restapi.dto.user;

import com.kiesoft.restapi.domain.user.User;
import com.kiesoft.restapi.dto.AbstractDTO;
import com.kiesoft.restapi.dto.role.RoleDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDTO extends AbstractDTO implements User<RoleDTO> {

    private String name;
    private String password;
    private Boolean enabled;
    private List<RoleDTO> roles = new ArrayList<>();
    private Integer points;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public List<RoleDTO> getRoles() {
        return roles;
    }

    @Override
    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public Integer getPoints() {
        return points;
    }

    @Override
    public void setPoints(Integer points) {
        this.points = points;
    }


    @Override
    public void addRole(RoleDTO role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }

    @Override
    public void removeRole(RoleDTO role) {
        this.roles.remove(role);
    }


    public static final class Builder {
        private UUID id;
        private String name;
        private String password;
        private Boolean enabled;
        private List<RoleDTO> roles = new ArrayList<>();
        private Integer points;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder roles(List<RoleDTO> roles) {
            this.roles = roles;
            return this;
        }

        public Builder points(Integer points) {
            this.points = points;
            return this;
        }

        public UserDTO build() {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(id);
            userDTO.setName(name);
            userDTO.setPassword(password);
            userDTO.setEnabled(enabled);
            userDTO.setRoles(roles);
            userDTO.setPoints(points);
            return userDTO;
        }
    }
}
