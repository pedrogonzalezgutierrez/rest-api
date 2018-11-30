package com.kiesoft.customer.jpa.repository.alias;

import com.kiesoft.customer.jpa.entity.alias.AliasEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AliasRepository extends PagingAndSortingRepository<AliasEntity, UUID> {

}
