package com.bacuti.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bacuti.service.dto.SupplierDTO;

/**
 * Handles company related CRUD.
 *
 * @author Imran Nazir K
 * @since 2024-06-28
 */
public interface SupplierService extends UploadService {

    /**
     * Saves Supplier.
     *
     * @param supplier to be saved.
     * @return saved supplier object.
     */
    SupplierDTO save(SupplierDTO supplier);

    /**
     * Gets the supplier by id.
     *
     * @param id of the supplier
     * @return founded supplier as dto.
     */
    SupplierDTO findById(Long id);

    /**
     * Gets all data by pagination.
     *
     * @param start - start value for pagination.
     * @param limit - limit value for pagination.
     * @param sortBy - sorting column name.
     * @param sortingOrder - sorting order to sort.
     * @param searchKey - search key to find data.
     * @return list of supplier as paged.
     */
    Page<SupplierDTO> getAllSuppliersPaged(int start, int limit, String sortBy, String sortingOrder, String searchKey);

    /**
     * Gets all data.
     *
     * @return list of supplier.
     */
    List<SupplierDTO> findAll();

    /**
     * Updates the supplier.
     *
     * @param supplier to be updated.
     * @return updated supplier.
     */
    SupplierDTO update(Long id, SupplierDTO supplier);

    /**
     * Deletes data by id.
     *
     * @param id of the supplier.
     */
    void deleteById(Long id);

    /**
     * Fetch All Supplier Name
     *
     * @return - Returns list of supplier name.
     */
    List<String> fetchAllSupplierNames();
}
