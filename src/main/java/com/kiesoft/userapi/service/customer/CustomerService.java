package com.kiesoft.userapi.service.customer;

import com.kiesoft.userapi.dto.customer.CustomerDTO;
import com.kiesoft.userapi.service.DeleteEntityService;
import com.kiesoft.userapi.service.FindAllPaginationService;
import com.kiesoft.userapi.service.FindOneService;
import com.kiesoft.userapi.service.SaveEntityService;

public interface CustomerService extends
        SaveEntityService<CustomerDTO>,
        DeleteEntityService<CustomerDTO>,
        FindOneService<CustomerDTO>,
        FindAllPaginationService<CustomerDTO> {

}
