package com.gvc.customer.converter.customer;

import com.gvc.customer.dto.customer.CustomerDTO;
import com.gvc.customer.jpa.entity.customer.CustomerEntity;
import fr.xebia.extras.selma.IgnoreFields;
import fr.xebia.extras.selma.Mapper;

@Mapper
public interface CustomerMapper {

    @IgnoreFields("customer")
    CustomerDTO asDTO(CustomerEntity customerEntity);

    CustomerEntity asEntity(CustomerDTO customerDTO);

}
