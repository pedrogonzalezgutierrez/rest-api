package com.gvc.customer.service.customer;

import com.gvc.customer.converter.alias.AliasMapper;
import com.gvc.customer.converter.customer.CustomerMapper;
import com.gvc.customer.dto.alias.AliasDTO;
import com.gvc.customer.dto.customer.CustomerDTO;
import com.gvc.customer.exception.PersistenceProblemException;
import com.gvc.customer.jpa.entity.customer.CustomerEntity;
import com.gvc.customer.jpa.repository.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "customerService")
public class DefaultCustomerService implements CustomerService {

    private final CustomerRepository repository;
    private final AliasMapper aliasMapper;
    private final CustomerMapper customerMapper;

    @Autowired
    public DefaultCustomerService(final CustomerRepository repository,
                                  final AliasMapper aliasMapper,
                                  final CustomerMapper customerMapper) {
        this.repository = repository;
        this.aliasMapper = aliasMapper;
        this.customerMapper = customerMapper;
    }

    @Override
    public void delete(CustomerDTO customerDTO) {
        try {
            repository.delete(customerMapper.asEntity(customerDTO));
        } catch (Exception exception) {
            throw new PersistenceProblemException(exception);
        }
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        try {
            List<AliasDTO> aliases = new ArrayList<>(customerDTO.getAliases());
            customerDTO.getAliases().clear();

            final CustomerEntity savedEntity = repository.save(customerMapper.asEntity(customerDTO));

            aliases.forEach(aliasDTO -> savedEntity.addAlias(aliasMapper.asEntity(aliasDTO)));

            return customerMapper.asDTO(repository.save(savedEntity));
        } catch (Exception exception) {
            throw new PersistenceProblemException(exception);
        }
    }

    @Override
    public Optional<CustomerDTO> findOne(Long id) {
        Optional<CustomerEntity> entity = repository.findById(id);
        return entity.map(customerMapper::asDTO);
    }

    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        Page<CustomerEntity> page = repository.findAll(pageable);
        List<CustomerDTO> listDTO = page.getContent().stream()
                .map(customerMapper::asDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(listDTO, pageable, page.getTotalElements());
    }

}
