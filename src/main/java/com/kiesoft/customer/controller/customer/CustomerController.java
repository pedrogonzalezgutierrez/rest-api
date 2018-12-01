package com.kiesoft.customer.controller.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kiesoft.customer.controller.customer.AbstractCustomerController.ROUTING_USER_CONTROLLER;

@RestController
@RequestMapping(ROUTING_USER_CONTROLLER)
public class CustomerController extends AbstractCustomerController {
//
//    private final CustomerService customerService;
//    private final CustomerDTOAliasesValidator customerDTOAliasesValidator;
//
//    @Autowired
//    public CustomerController(final CustomerService customerService, final CustomerDTOAliasesValidator customerDTOAliasesValidator) {
//        this.customerService = customerService;
//        this.customerDTOAliasesValidator = customerDTOAliasesValidator;
//    }
//
//    @InitBinder("customerDTO")
//    public void setupBinder(WebDataBinder binder) {
//        binder.addValidators(customerDTOAliasesValidator);
//    }
//
//    @RequestMapping(value = ROUTING_FIND_ONE, method = RequestMethod.GET)
//    public ResponseEntity<CustomerDTO> findOne(@PathVariable UUID id) {
//        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
//        return customerDTO
//                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @RequestMapping(value = ROUTING_FIND_ALL, method = RequestMethod.GET)
//    public ResponseEntity<Page<CustomerDTO>> findAll(@PageableDefault Pageable pageable) {
//        return new ResponseEntity<>(customerService.findAll(pageable), HttpStatus.OK);
//    }
//
//    @RequestMapping(value = ROUTING_CREATE_WITH_ALIASES, method = RequestMethod.POST)
//    public ResponseEntity<CustomerDTO> createWithAliases(@Valid @RequestBody CustomerDTO customerDTO) {
//        return new ResponseEntity<>(customerService.save(customerDTO), HttpStatus.OK);
//    }

}
