package com.kiesoft.customer.service.customer;

import com.kiesoft.customer.dto.customer.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service(value = "customerService")
public class DefaultCustomerService implements CustomerService {
    @Override
    public void delete(CustomerDTO customerDTO) {

    }

    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<CustomerDTO> findOne(UUID id) {
        return Optional.empty();
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        return null;
    }

//    private final CustomerRepository repository;
//    private final AliasMapper aliasMapper;
//    private final CustomerMapper customerMapper;
//
//    @Autowired
//    public DefaultCustomerService(final CustomerRepository repository,
//                                  final AliasMapper aliasMapper,
//                                  final CustomerMapper customerMapper) {
//        this.repository = repository;
//        this.aliasMapper = aliasMapper;
//        this.customerMapper = customerMapper;
//    }
//
//    @Override
//    public void delete(CustomerDTO customerDTO) {
//        try {
//            repository.delete(customerMapper.asEntity(customerDTO));
//        } catch (Exception exception) {
//            throw new PersistenceProblemException(exception);
//        }
//    }
//
//    @Override
//    public CustomerDTO save(CustomerDTO customerDTO) {
//        try {
//            List<AliasDTO> aliases = new ArrayList<>(customerDTO.getAliases());
//            customerDTO.getAliases().clear();
//
//            final CustomerEntity savedEntity = repository.save(customerMapper.asEntity(customerDTO));
//
//            aliases.forEach(aliasDTO -> savedEntity.addAlias(aliasMapper.asEntity(aliasDTO)));
//
//            return customerMapper.asDTO(repository.save(savedEntity));
//        } catch (Exception exception) {
//            throw new PersistenceProblemException(exception);
//        }
//    }
//
//    @Override
//    public Optional<CustomerDTO> findOne(UUID id) {
//        Optional<CustomerEntity> entity = repository.findById(id);
//        return entity.map(customerMapper::asDTO);
//    }
//
//    @Override
//    public Page<CustomerDTO> findAll(Pageable pageable) {
//        Page<CustomerEntity> page = repository.findAll(pageable);
//        List<CustomerDTO> listDTO = page.getContent().stream()
//                .map(customerMapper::asDTO)
//                .collect(Collectors.toList());
//        return new PageImpl<>(listDTO, pageable, page.getTotalElements());
//    }

}
