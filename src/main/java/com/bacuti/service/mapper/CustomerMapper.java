package com.bacuti.service.mapper;

import com.bacuti.domain.Customer;
import com.bacuti.security.SecurityUtils;
import com.bacuti.service.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.Objects;

/**
 * Customer Mapper file
 *
 * @author Premkalyan Sekar
 * @since 03/07/2023
 * @version 1.0
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    // Convert customer dto to customer entity
    @Mappings({
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "lastModifiedBy", ignore = true),
        @Mapping(target = "lastModifiedDate", ignore = true)
    })
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    // Convert customer entity to customer data transfer object.
    CustomerDTO customerToCustomerDTO(Customer customer);

    /**
     * Method used to set the created and modified values in entity.
     * @param customer - Input customer entity
     */
    default void updateAuditColumns(Customer customer) {
        String username = SecurityUtils.getCurrentUserLogin().get();
        Instant now = Instant.now();

        if (Objects.isNull(customer.getId())) {
            // New entity
            customer.setCreatedBy(username);
            customer.setCreatedDate(now);
        }
        // Update last modified info
        customer.setLastModifiedBy(username);
        customer.setLastModifiedDate(now);
    }
}
