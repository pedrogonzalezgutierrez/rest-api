package com.kiesoft.customer.converter.alias;

import com.kiesoft.customer.dto.alias.AliasDTO;
import com.kiesoft.customer.jpa.entity.alias.AliasEntity;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreFields = "customer")
public interface AliasMapper {

    AliasDTO asDTO(AliasEntity aliasEntity);

    AliasEntity asEntity(AliasDTO aliasDTO);

}
