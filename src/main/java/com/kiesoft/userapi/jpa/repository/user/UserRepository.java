package com.kiesoft.userapi.jpa.repository.user;

import com.kiesoft.userapi.jpa.entity.user.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {

//    UserEntity findByName(String name);
    Optional<UserEntity> findByName(String name);


}
