package com.bacuti.service;

import com.bacuti.service.dto.CustomerDTO;
import org.springframework.data.domain.Page;

/**
 * Handel business logic and service api related to customer
 *
 * @author Premkalyan Sekar
 * @since 03/07/2023
 * @version 1.0
 */
public interface CustomerService extends UploadService {

    /**
     * Method used to create customer entity.
     *
     * @param customerDTO - Input customer dto
     * @return - Returns the created customer dto
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    /**
     * Method used to fetch the  customer details.
     * @param pageNo - Page number to fetch data.
     * @param pageSize - Page size of screen
     * @param sortBy - Data sorting column
     * @param sortDirection - Data sort direction
     * @param customerName - Customer Name column to search value.
     * @return Returns Item supplier dto
     */
    Page<CustomerDTO> getCustomers(int pageNo, int pageSize, String sortBy, String sortDirection, String customerName);

    /**
     * Method used to delete the customer entity by id.
     * @param id - Customer id.
     */
    void deleteCustomerById(Long id);
}
