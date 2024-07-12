package com.bacuti.web.rest;

import com.bacuti.service.CustomerService;
import com.bacuti.service.dto.CustomerDTO;
import com.bacuti.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.web.util.HeaderUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * REST controller for managing {@link com.bacuti.domain.Customer}.
 *
 * @author Premkalyan Sekar
 * @since 03/07/2023
 * @version 1.0
 */
@RestController
@RequestMapping("/api/${app.api.version}/customers")
@Transactional
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "uploadServiceCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CustomerService customerService;

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customer the customer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customer,
     * or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customer) throws URISyntaxException {
        log.debug("REST request to save Customer");
        if (customer.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customer = customerService.createCustomer(customer);
        return ResponseEntity.created(new URI("/api/customers/" + customer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            customer.getId().toString())).body(customer);
    }

    /**
     * {@code PUT  /customers/:id} : Updates an existing customer.
     *
     * @param id the id of the customer to save.
     * @param customer the customer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customer,
     * or with status {@code 400 (Bad Request)} if the customer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customer couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable(value = "id", required = true) final Long id,
        @Valid @RequestBody CustomerDTO customer) {
        log.debug("REST request to update Customer");

        if (Objects.isNull(customer.getId()) || !Objects.equals(id, customer.getId())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "Id Null or Wrong");
        }
        customer = customerService.createCustomer(customer);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                true, ENTITY_NAME, customer.getId().toString())).body(customer);
    }

    /**
     *{@code GET  /customers} : get all the customers.
     *
     * @param pageNo - Page number to fetch data.
     * @param pageSize - Page size of screen
     * @param sortBy - Data sorting column
     * @param sortDirection - Data sort direction
     * @param customerName - Customer name column.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("")
    public ResponseEntity<Page<CustomerDTO>> getCustomers(
        @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(defaultValue = "lastModifiedDate") String sortBy, @RequestParam(defaultValue = "desc") String sortDirection,
        @RequestParam(defaultValue = "") String customerName) {
        return ResponseEntity.ok(customerService.getCustomers(pageNo, pageSize, sortBy, sortDirection, customerName));
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable("id") Long id) {
        log.debug("REST request to delete Customer");
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok().body(true);
    }
}
