package com.kiesoft.userapi.domain.user;

import com.kiesoft.userapi.domain.BaseEntity;

import java.util.List;

public interface User<T> extends BaseEntity {

    String getName();

    void setName(String name);

    String getPassword();

    void setPassword(String password);

    Boolean getEnabled();

    void setEnabled(Boolean enabled);

    List<T> getRoles();

    void setRoles(List<T> roles);

    Integer getPoints();

    void setPoints(Integer points);

    void addRole(T role);

    void removeRole(T role);

}
