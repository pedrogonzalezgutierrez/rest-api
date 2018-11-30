package com.kiesoft.customer.jpa.repository.customer;

import com.kiesoft.customer.jpa.entity.customer.CustomerEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, UUID> {
}
