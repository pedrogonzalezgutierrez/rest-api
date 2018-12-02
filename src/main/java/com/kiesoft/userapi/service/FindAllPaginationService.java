package com.kiesoft.userapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllPaginationService<Entity> {

    Page<Entity> findAll(Pageable pageable);

}
