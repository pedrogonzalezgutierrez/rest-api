package com.gvc.customer.service;

import java.util.Optional;

public interface FindOneService<Entity> {

    Optional<Entity> findOne(Long id);

}
