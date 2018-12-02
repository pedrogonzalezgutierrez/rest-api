package com.kiesoft.userapi.jpa.repository.alias;

import com.kiesoft.userapi.jpa.entity.alias.AliasEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AliasRepository extends PagingAndSortingRepository<AliasEntity, UUID> {

}
