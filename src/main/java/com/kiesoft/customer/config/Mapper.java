package com.kiesoft.customer.config;

import com.kiesoft.customer.converter.alias.AliasMapper;
import com.kiesoft.customer.converter.customer.CustomerMapper;
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
