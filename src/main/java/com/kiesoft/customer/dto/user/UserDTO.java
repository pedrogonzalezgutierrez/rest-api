package com.kiesoft.customer.dto.user;

import com.kiesoft.customer.domain.user.User;
import com.kiesoft.customer.dto.AbstractDTO;
import com.kiesoft.customer.dto.role.RoleDTO;

import java.util.ArrayList;
import java.util.List;

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

}
