package com.gvc.customer.converter.customer;

import com.gvc.customer.dto.customer.CustomerDTO;
import com.gvc.customer.jpa.entity.customer.CustomerEntity;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;

@Mapper
public interface CustomerMapper {

    @Maps(withIgnoreFields = "customer")
    CustomerDTO asDTO(CustomerEntity customerEntity);

    CustomerEntity asEntity(CustomerDTO customerDTO);

}
