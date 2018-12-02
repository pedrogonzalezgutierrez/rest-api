package com.kiesoft.userapi.jpa.repository.role;

import com.kiesoft.userapi.jpa.entity.role.RoleEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, UUID> {

}
