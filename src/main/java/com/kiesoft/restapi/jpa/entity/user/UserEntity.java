package com.kiesoft.restapi.jpa.entity.user;

import com.kiesoft.restapi.domain.user.User;
import com.kiesoft.restapi.jpa.entity.AbstractEntity;
import com.kiesoft.restapi.jpa.entity.role.RoleEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "userapi_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class UserEntity extends AbstractEntity implements User<RoleEntity> {

    private String name;
    private String password;
    private Boolean enabled;
    private List<RoleEntity> roles = new ArrayList<>();
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "userapi_user_roles",
            joinColumns = {@JoinColumn(name = "idUser")},
            inverseJoinColumns = {@JoinColumn(name = "idRole")})
    @Override
    public List<RoleEntity> getRoles() {
        return roles;
    }

    @Override
    public void setRoles(List<RoleEntity> roles) {
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
    public void addRole(RoleEntity role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }

    @Override
    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
    }

    public static final class Builder {
        private UUID id;
        private String name;
        private String password;
        private Boolean enabled;
        private List<RoleEntity> roles = new ArrayList<>();
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

        public Builder roles(List<RoleEntity> roles) {
            this.roles = roles;
            return this;
        }

        public Builder points(Integer points) {
            this.points = points;
            return this;
        }

        public UserEntity build() {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(id);
            userEntity.setName(name);
            userEntity.setPassword(password);
            userEntity.setEnabled(enabled);
            userEntity.setRoles(roles);
            userEntity.setPoints(points);
            return userEntity;
        }
    }

}
