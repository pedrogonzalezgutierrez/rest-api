package com.kiesoft.restapi.domain.alias;

import com.kiesoft.restapi.domain.BaseEntity;

public interface Alias<T> extends BaseEntity {

    String getName();

    void setName(String name);

    String getShopId();

    void setShopId(String shopId);

    Boolean getArchived();

    void setArchived(Boolean archived);

    T getCustomer();

    void setCustomer(T customer);

}
