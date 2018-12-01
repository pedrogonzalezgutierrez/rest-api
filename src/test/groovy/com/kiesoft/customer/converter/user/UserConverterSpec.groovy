package com.kiesoft.customer.converter.user

import com.kiesoft.customer.jpa.entity.role.RoleEntity
import com.kiesoft.customer.jpa.entity.user.UserEntity
import fr.xebia.extras.selma.Selma
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import spock.lang.Specification

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserConverterSpec extends Specification {

    final userConverter = Selma.getMapper(UserConverter.class);
//    final userConverter = Selma.builder(UserConverter.class).build()

    def "will convert to DTO an Entity"() {
        given:
        final roleEntity = new RoleEntity.Builder()
                .id(UUID.randomUUID())
                .name("ROLE_ADMIN")
                .build()

        final userEntity = new UserEntity.Builder()
                .id(UUID.randomUUID())
                .name("Pedro")
                .password("Real Betis")
                .enabled(Boolean.TRUE)
                .roles(Arrays.asList(roleEntity))
                .points(100)
                .build()

        when:
        def userDTO = userConverter.asDTO(userEntity)

        then:
        with(userDTO) {
            id == userEntity.id
            password == userEntity.password
            enabled == userEntity.enabled
            points == userEntity.points
        }
    }

}
