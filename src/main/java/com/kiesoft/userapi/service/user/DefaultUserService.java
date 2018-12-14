package com.kiesoft.userapi.service.user;

import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.exception.PersistenceProblemException;
import com.kiesoft.userapi.jpa.entity.user.UserEntity;
import com.kiesoft.userapi.jpa.repository.user.UserRepository;
import com.kiesoft.userapi.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service(value = "userService")
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public DefaultUserService(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        try {
            return userMapper.asDTO(userRepository.save(userMapper.asEntity(userDTO)));
        } catch (Exception e) {
            throw new PersistenceProblemException(ApiErrorMessage.USER_NOT_SAVED.getMessage(), e);
        }
    }

    @Override
    public Optional<UserDTO> findByName(String name) {
        Optional<UserEntity> userEntity = userRepository.findByNameIgnoreCase(name);
        return userEntity.map(userMapper::asDTO);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmailIgnoreCase(email);
        return userEntity.map(userMapper::asDTO);
    }

    @Override
    public Optional<UserDTO> findByEmailAndPassword(String email, String password) {
        Optional<UserEntity> userEntity = userRepository.findByEmailIgnoreCaseAndPassword(email, password);
        return userEntity.map(userMapper::asDTO);
    }

    @Override
    public Optional<UserDTO> findById(UUID id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(userMapper::asDTO);
    }

}
