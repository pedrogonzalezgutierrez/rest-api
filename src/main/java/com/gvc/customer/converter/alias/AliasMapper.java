package com.gvc.customer.converter.alias;

import com.gvc.customer.dto.alias.AliasDTO;
import com.gvc.customer.jpa.entity.alias.AliasEntity;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIgnoreFields = "customer")
public interface AliasMapper {

    AliasDTO asDTO(AliasEntity aliasEntity);

    AliasEntity asEntity(AliasDTO aliasDTO);

}
