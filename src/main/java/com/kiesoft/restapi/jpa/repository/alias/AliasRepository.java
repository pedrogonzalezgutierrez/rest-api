package com.kiesoft.restapi.jpa.repository.alias;

import com.kiesoft.restapi.jpa.entity.alias.AliasEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AliasRepository extends PagingAndSortingRepository<AliasEntity, UUID> {

}
