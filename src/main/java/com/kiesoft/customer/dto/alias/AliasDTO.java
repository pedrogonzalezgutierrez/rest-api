package com.kiesoft.customer.dto.alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kiesoft.customer.domain.alias.Alias;
import com.kiesoft.customer.dto.AbstractDTO;
import com.kiesoft.customer.dto.customer.CustomerDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AliasDTO extends AbstractDTO implements Alias<CustomerDTO> {

    private String name;
    private String shopId;
    private Boolean archived;
    private CustomerDTO customer;

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

    @Override
    public CustomerDTO getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

}
