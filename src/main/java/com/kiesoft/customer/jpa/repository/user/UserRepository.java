package com.kiesoft.customer.jpa.repository.user;

import com.kiesoft.customer.jpa.entity.user.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {

}
