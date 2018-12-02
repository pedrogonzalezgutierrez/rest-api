package com.kiesoft.userapi.jpa.repository.customer;

import com.kiesoft.userapi.jpa.entity.customer.CustomerEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, UUID> {
}
