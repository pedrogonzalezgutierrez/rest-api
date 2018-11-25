package com.gvc.customer.config;

import com.gvc.customer.converter.alias.AliasMapper;
import com.gvc.customer.converter.customer.CustomerMapper;
import fr.xebia.extras.selma.Selma;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mapper {

    @Bean
    public AliasMapper aliasMapper() {
        return Selma.builder(AliasMapper.class).build();
    }

    @Bean
    public CustomerMapper customerMapper() {
        return Selma.builder(CustomerMapper.class).build();
    }

}
