package com.kiesoft.customer.jpa.entity.role;

import com.kiesoft.customer.domain.role.Role;
import com.kiesoft.customer.jpa.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Table(name = "userapi_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class RoleEntity extends AbstractEntity implements Role {

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

        public RoleEntity build() {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setId(id);
            roleEntity.setName(name);
            return roleEntity;
        }
    }
}
