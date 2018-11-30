package com.kiesoft.customer.jpa.entity.customer;

import com.kiesoft.customer.domain.customer.Customer;
import com.kiesoft.customer.jpa.entity.AbstractEntity;
import com.kiesoft.customer.jpa.entity.alias.AliasEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_customer")
public class CustomerEntity extends AbstractEntity implements Customer<AliasEntity> {

    private String firstName;
    private String lastName;
    private List<AliasEntity> aliases = new ArrayList<>();

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @Override
    public List<AliasEntity> getAliases() {
        return aliases;
    }

    @Override
    public void setAliases(List<AliasEntity> aliases) {
        this.aliases = aliases;
    }

    @Override
    public void addAlias(AliasEntity alias) {
        alias.setCustomer(this);
        this.aliases.add(alias);
    }

    @Override
    public void removeAlias(AliasEntity alias) {
        alias.setCustomer(null);
        this.aliases.remove(alias);
    }

}
