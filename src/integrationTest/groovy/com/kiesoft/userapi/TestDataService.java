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
import java.util.Optional;

/**
 * Service used only for create dummy data in the Integration Tests
 */
@Component
public class TestDataService {

    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    public final static String ROLE_EDITOR = "ROLE_EDITOR";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public TestDataService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public RoleEntity roleAdmin() {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByNameIgnoreCase(ROLE_ADMIN);
        if(optionalRoleEntity.isPresent()) {
            return optionalRoleEntity.get();
        } else {
            RoleEntity roleAdmin = new RoleEntity.Builder()
                    .name(ROLE_ADMIN)
                    .build();
            return roleRepository.save(roleAdmin);
        }
    }

    public RoleEntity roleEditor() {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByNameIgnoreCase(ROLE_EDITOR);
        if(optionalRoleEntity.isPresent()) {
            return optionalRoleEntity.get();
        } else {
            RoleEntity roleEditor = new RoleEntity.Builder()
                    .name(ROLE_EDITOR)
                    .build();
            return roleRepository.save(roleEditor);
        }
    }

    public UserEntity userAdmin() {
        RoleEntity roleAdmin = roleAdmin();

        UserEntity userAdmin = new UserEntity.Builder()
                .name("admin")
                .email("admin@kiesoft.com")
                .password("21232f297a57a5a743894a0e4a801fc3") // admin
                .enabled(Boolean.TRUE)
                .points(2000)
                .roles(Collections.singletonList(roleAdmin))
                .build();
        return userRepository.save(userAdmin);
    }

    public UserEntity userEditor() {
        RoleEntity roleEditor = roleEditor();

        UserEntity userEditor = new UserEntity.Builder()
                .name("editor")
                .email("editor@kiesoft.com")
                .password("5aee9dbd2a188839105073571bee1b1f") // editor
                .enabled(Boolean.TRUE)
                .points(1000)
                .roles(Collections.singletonList(roleEditor))
                .build();
        return userRepository.save(userEditor);
    }

    public List<UserEntity> usersAdminAndEditor() {
        return Arrays.asList(userAdmin(), userEditor());
    }

    public UserEntity userPedrola() {
        UserEntity pedrola = new UserEntity.Builder()
                .name("pedrola")
                .email("pedrola@kiesoft.com")
                .password("c25894ebba77ba392a5f9a67354ca257") // pedrola
                .enabled(Boolean.TRUE)
                .points(0)
                .build();
        return userRepository.save(pedrola);
    }

    public UserEntity userMadara() {
        UserEntity madara = new UserEntity.Builder()
                .name("madara")
                .email("madara@kiesoft.com")
                .password("ca0ac6f71f426aa63a4bbfabea06d1a2") // madara
                .enabled(Boolean.TRUE)
                .points(500)
                .build();
        return userRepository.save(madara);
    }

    public UserEntity userSuColega() {
        UserEntity sucolega = new UserEntity.Builder()
                .name("sucolega")
                .email("sucolega@kiesoft.com")
                .password("9e522220f3bc58fa75c98e4b6a5c95e9") // sucolega
                .enabled(Boolean.FALSE)
                .points(0)
                .build();
        return userRepository.save(sucolega);
    }

    public List<UserEntity> usersRandomPeople() {
        return Arrays.asList(userPedrola(), userMadara(), userSuColega());
    }

}
