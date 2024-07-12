package com.bacuti.service;

import com.bacuti.service.dto.ItemSupplierDTO;
import org.springframework.data.domain.Page;

public interface ItemSupplierService extends UploadService {

    /**
     * Method used to create the Item Supplier entity.
     * @param itemSupplierDTO - Item supplier dto from input params
     * @return - Created item object
     */
    ItemSupplierDTO createItemSupplier(ItemSupplierDTO itemSupplierDTO);

    /**
     * Method used to delete the item supplier entity by itemsupplierid
     * @param id - Input itemsupplier id.
     */
    void deleteItemSupplierById(Long id);

    /**
     * Method used to fetch the Item supplier details.
     * @param pageNo - Page number to fetch data.
     * @param pageSize - Page size of screen
     * @param sortBy - Data sorting column
     * @param sortDirection - Data sort direction
     * @param supplierOwnItem - Supplier own item column.
     * @return Returns Item supplier dto
     */
    Page<ItemSupplierDTO> getItemSupplier(int pageNo, int pageSize, String sortBy, String sortDirection, String supplierOwnItem);
}
