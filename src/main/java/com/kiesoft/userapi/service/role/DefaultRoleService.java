package com.kiesoft.userapi.service.role;

import com.kiesoft.userapi.controller.error.ApiErrorMessage;
import com.kiesoft.userapi.dto.role.RoleDTO;
import com.kiesoft.userapi.dto.user.UserDTO;
import com.kiesoft.userapi.exception.PersistenceProblemException;
import com.kiesoft.userapi.jpa.entity.role.RoleEntity;
import com.kiesoft.userapi.jpa.entity.user.UserEntity;
import com.kiesoft.userapi.jpa.repository.role.RoleRepository;
import com.kiesoft.userapi.mapper.role.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service(value = "roleService")
public class DefaultRoleService implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public DefaultRoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        try {
            return roleMapper.asDTO(roleRepository.save(roleMapper.asEntity(roleDTO)));
        } catch (Exception e) {
            throw new PersistenceProblemException(ApiErrorMessage.ROLE_NOT_SAVED.getMessage(), e);
        }
    }

    @Override
    public void delete(RoleDTO roleDTO) {
        try {
            roleRepository.deleteById(roleDTO.getId());
        } catch (Exception e) {
            throw new PersistenceProblemException(ApiErrorMessage.ROLE_NOT_DELETED.getMessage(), e);
        }
    }

    @Override
    public Optional<RoleDTO> findByName(String name) {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameIgnoreCase(name);
        return roleEntity.map(roleMapper::asDTO);

    }

    @Override
    public Optional<RoleDTO> findById(UUID id) {
        Optional<RoleEntity> roleEntity = roleRepository.findById(id);
        return roleEntity.map(roleMapper::asDTO);
    }

}
