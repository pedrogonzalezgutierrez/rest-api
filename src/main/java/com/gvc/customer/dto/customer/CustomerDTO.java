package com.gvc.customer.dto.customer;

import com.gvc.customer.domain.customer.Customer;
import com.gvc.customer.dto.AbstractDTO;
import com.gvc.customer.dto.alias.AliasDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomerDTO extends AbstractDTO implements Customer<AliasDTO> {

    private String firstName;
    private String lastName;
    private List<AliasDTO> aliases = new ArrayList<>();

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public List<AliasDTO> getAliases() {
        return aliases;
    }

    @Override
    public void setAliases(List<AliasDTO> aliases) {
        this.aliases = aliases;
    }

    @Override
    public void addAlias(AliasDTO alias) {
        alias.setCustomer(this);
        this.aliases.add(alias);
    }

    @Override
    public void removeAlias(AliasDTO alias) {
        alias.setCustomer(null);
        this.aliases.remove(alias);
    }

}
