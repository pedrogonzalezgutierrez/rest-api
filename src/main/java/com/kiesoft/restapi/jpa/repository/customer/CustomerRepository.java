package com.kiesoft.restapi.jpa.repository.customer;

import com.kiesoft.restapi.jpa.entity.customer.CustomerEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, UUID> {
}
