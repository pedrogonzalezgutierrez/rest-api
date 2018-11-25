package com.gvc.customer.jpa.repository.customer;

import com.gvc.customer.jpa.entity.customer.CustomerEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, Long> {
}
