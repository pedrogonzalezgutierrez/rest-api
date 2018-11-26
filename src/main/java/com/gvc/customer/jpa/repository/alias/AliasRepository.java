package com.gvc.customer.jpa.repository.alias;

import com.gvc.customer.jpa.entity.alias.AliasEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AliasRepository extends PagingAndSortingRepository<AliasEntity, UUID> {

}
