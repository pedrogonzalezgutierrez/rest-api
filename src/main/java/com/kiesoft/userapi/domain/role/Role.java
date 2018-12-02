package com.kiesoft.userapi.domain.role;

import com.kiesoft.userapi.domain.BaseEntity;

public interface Role extends BaseEntity {

    String getName();

    void setName(String name);

}
