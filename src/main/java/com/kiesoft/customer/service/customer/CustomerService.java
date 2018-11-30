package com.kiesoft.customer.service.customer;

import com.kiesoft.customer.dto.customer.CustomerDTO;
import com.kiesoft.customer.service.DeleteEntityService;
import com.kiesoft.customer.service.FindAllPaginationService;
import com.kiesoft.customer.service.FindOneService;
import com.kiesoft.customer.service.SaveEntityService;

public interface CustomerService extends
        SaveEntityService<CustomerDTO>,
        DeleteEntityService<CustomerDTO>,
        FindOneService<CustomerDTO>,
        FindAllPaginationService<CustomerDTO> {

}
