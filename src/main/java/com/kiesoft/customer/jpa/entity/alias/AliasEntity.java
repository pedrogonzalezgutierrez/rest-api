package com.kiesoft.customer.jpa.entity.alias;

import com.kiesoft.customer.domain.alias.Alias;
import com.kiesoft.customer.jpa.entity.AbstractEntity;
import com.kiesoft.customer.jpa.entity.customer.CustomerEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_alias")
public class AliasEntity extends AbstractEntity implements Alias<CustomerEntity> {

    private String name;
    private String shopId;
    private Boolean archived;
    private CustomerEntity customer;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getShopId() {
        return shopId;
    }

    @Override
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public Boolean getArchived() {
        return archived;
    }

    @Override
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    @ManyToOne
    @Override
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

}
