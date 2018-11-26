package com.gvc.customer.controller.customer;

import com.gvc.customer.dto.customer.CustomerDTO;
import com.gvc.customer.service.customer.CustomerService;
import com.gvc.customer.validator.customer.CustomerDTOAliasesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static com.gvc.customer.controller.customer.AbstractCustomerController.ROUTING_USER_CONTROLLER;

@RestController
@RequestMapping(ROUTING_USER_CONTROLLER)
public class CustomerController extends AbstractCustomerController {

    private final CustomerService customerService;
    private final CustomerDTOAliasesValidator customerDTOAliasesValidator;

    @Autowired
    public CustomerController(final CustomerService customerService, final CustomerDTOAliasesValidator customerDTOAliasesValidator) {
        this.customerService = customerService;
        this.customerDTOAliasesValidator = customerDTOAliasesValidator;
    }

    @InitBinder("customerDTO")
    public void setupBinder(WebDataBinder binder) {
        binder.addValidators(customerDTOAliasesValidator);
    }

    @RequestMapping(value = ROUTING_FIND_ONE, method = RequestMethod.GET)
    public ResponseEntity<CustomerDTO> findOne(@PathVariable UUID id) {
        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
        return customerDTO
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = ROUTING_FIND_ALL, method = RequestMethod.GET)
    public ResponseEntity<Page<CustomerDTO>> findAll(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(customerService.findAll(pageable), HttpStatus.OK);
    }

    @RequestMapping(value = ROUTING_CREATE_WITH_ALIASES, method = RequestMethod.POST)
    public ResponseEntity<CustomerDTO> createWithAliases(@Valid @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<>(customerService.save(customerDTO), HttpStatus.OK);
    }

}
