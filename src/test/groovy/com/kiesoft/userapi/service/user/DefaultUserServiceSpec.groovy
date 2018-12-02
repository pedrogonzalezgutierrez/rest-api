package com.kiesoft.userapi.service.user

import com.kiesoft.userapi.dto.user.UserDTO
import com.kiesoft.userapi.exception.PersistenceProblemException
import com.kiesoft.userapi.jpa.entity.user.UserEntity
import com.kiesoft.userapi.jpa.repository.user.UserRepository
import com.kiesoft.userapi.mapper.user.UserMapper
import spock.lang.Specification

class DefaultUserServiceSpec extends Specification {

    final userRepository = Mock(UserRepository)
    final userMapper = Mock(UserMapper)
    final userService = new DefaultUserService(userRepository, userMapper)

    final username = "pEDROLA"
    final userDTO = new UserDTO.Builder()
            .name(username)
            .build()

    final userEntity = new UserEntity.Builder()
            .name(username)
            .build()

    UUID idUser = UUID.randomUUID()

    final savedEntity = new UserEntity.Builder()
            .id(idUser)
            .name(username)
            .build()

    final savedDTO = new UserDTO.Builder()
            .id(idUser)
            .name(username)
            .build()

    def "create a user"() {
        given:
        userMapper.asEntity(userDTO) >> userEntity
        userRepository.save(userEntity) >> savedEntity
        userMapper.asDTO(savedEntity) >> savedDTO

        when:
        final actual = userService.save(userDTO)

        then:
        actual == savedDTO
    }

    def "error creating a user when persisting DB"() {
        given:
        userMapper.asEntity(userDTO) >> userEntity
        userRepository.save(userEntity) >> { throw new RuntimeException() }

        when:
        userService.save(userDTO)

        then:
        thrown PersistenceProblemException
    }

    def "findByName: user does not exist"() {
        given:
        userRepository.findByName("pEDROLA") >> Optional.empty()

        when:
        final optionalUserDTO = userService.findByName(username)

        then:
        !optionalUserDTO.isPresent()
    }

    def "findByName: user exists"() {
        given:
        userRepository.findByName("pEDROLA") >> Optional.of(savedEntity)
        userMapper.asDTO(savedEntity) >> savedDTO

        when:
        final optionalUserDTO = userService.findByName(username)

        then:
        optionalUserDTO.isPresent()
    }


}
