package com.kiesoft.restapi.jpa.entity;

import com.kiesoft.restapi.domain.BaseEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public class AbstractEntity implements BaseEntity {

    private UUID id;

    @Id
    @GeneratedValue
    @Override
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
