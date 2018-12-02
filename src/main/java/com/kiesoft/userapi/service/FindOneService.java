package com.kiesoft.userapi.service;

import java.util.Optional;
import java.util.UUID;

public interface FindOneService<Entity> {

    Optional<Entity> findOne(UUID id);

}
