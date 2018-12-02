package com.kiesoft.userapi.dto;

import com.kiesoft.userapi.domain.BaseEntity;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractDTO implements BaseEntity {

    private UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDTO that = (AbstractDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
