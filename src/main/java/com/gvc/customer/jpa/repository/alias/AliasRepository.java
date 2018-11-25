package com.gvc.customer.jpa.repository.alias;

import com.gvc.customer.jpa.entity.alias.AliasEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AliasRepository extends PagingAndSortingRepository<AliasEntity, Long> {

}
