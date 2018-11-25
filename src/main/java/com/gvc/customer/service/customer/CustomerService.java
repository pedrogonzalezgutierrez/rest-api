package com.gvc.customer.service.customer;

import com.gvc.customer.dto.customer.CustomerDTO;
import com.gvc.customer.service.DeleteEntityService;
import com.gvc.customer.service.FindAllPaginationService;
import com.gvc.customer.service.FindOneService;
import com.gvc.customer.service.SaveEntityService;

public interface CustomerService extends
        SaveEntityService<CustomerDTO>,
        DeleteEntityService<CustomerDTO>,
        FindOneService<CustomerDTO>,
        FindAllPaginationService<CustomerDTO> {

}
