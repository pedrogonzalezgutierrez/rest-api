package com.kiesoft.restapi.service.customer;

import com.kiesoft.restapi.dto.customer.CustomerDTO;
import com.kiesoft.restapi.service.DeleteEntityService;
import com.kiesoft.restapi.service.FindAllPaginationService;
import com.kiesoft.restapi.service.FindOneService;
import com.kiesoft.restapi.service.SaveEntityService;

public interface CustomerService extends
        SaveEntityService<CustomerDTO>,
        DeleteEntityService<CustomerDTO>,
        FindOneService<CustomerDTO>,
        FindAllPaginationService<CustomerDTO> {

}
