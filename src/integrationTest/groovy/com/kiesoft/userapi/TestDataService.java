package com.kiesoft.userapi;

import com.kiesoft.userapi.jpa.entity.role.RoleEntity;
import com.kiesoft.userapi.jpa.entity.user.UserEntity;
import com.kiesoft.userapi.jpa.repository.role.RoleRepository;
import com.kiesoft.userapi.jpa.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TestDataService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public TestDataService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public List<UserEntity> usersAdminAndEditor() {
        RoleEntity roleAdmin = new RoleEntity.Builder()
                .name("ROLE_ADMIN")
                .build();

        RoleEntity roleEditor = new RoleEntity.Builder()
                .name("ROLE_EDITOR")
                .build();

        roleAdmin = roleRepository.save(roleAdmin);
        roleEditor = roleRepository.save(roleEditor);

        UserEntity userAdmin = new UserEntity.Builder()
                .name("admin")
                .password("admin")
                .enabled(Boolean.TRUE)
                .points(2000)
                .roles(Collections.singletonList(roleAdmin))
                .build();

        UserEntity userEditor = new UserEntity.Builder()
                .name("editor")
                .password("editor")
                .enabled(Boolean.TRUE)
                .points(1000)
                .roles(Collections.singletonList(roleEditor))
                .build();

        userAdmin = userRepository.save(userAdmin);
        userEditor = userRepository.save(userEditor);

        return Arrays.asList(userAdmin, userEditor);
    }

    public List<UserEntity> usersRandomPeople() {

        UserEntity pedrola = new UserEntity.Builder()
                .name("pedrola")
                .password("pedrola")
                .enabled(Boolean.TRUE)
                .points(0)
                .build();

        UserEntity madara = new UserEntity.Builder()
                .name("madara")
                .password("madara")
                .enabled(Boolean.TRUE)
                .points(500)
                .build();

        pedrola = userRepository.save(pedrola);
        madara = userRepository.save(madara);

        return Arrays.asList(pedrola, madara);
    }

}
