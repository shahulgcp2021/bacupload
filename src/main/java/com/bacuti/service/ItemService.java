package com.bacuti.service;

import com.bacuti.service.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService extends UploadService {

    /**
     * Method used to create or update a Item.
     *
     * @param itemDTO - Item Object to create.
     * @return - Returns Created or updated item object.
     */
    ItemDTO saveItem(ItemDTO itemDTO);

    ItemDTO saveItemForOtherEntities(ItemDTO itemDTO);

    /**
     * Delete the item by id.
     *
     * @param id - Item id.
     */
    void deleteItemById(Long id);

    /**
     * Retrieves all items with pagination support.
     *
     * @param pageable - pagination information.
     * @return - Returns Page of item object.
     */
    Page<ItemDTO> getAllItems(Pageable pageable);

    /**
     * Fetch item category by filter value and page info.
     *
     * @param filterValue - Input filter value.
     * @param pageable - Pagination information.
     * @return - Returns item object
     */
    Page<ItemDTO> getItemCategoryFilter(String filterValue, Pageable pageable);

    /**
     * Fetch Item category
     *
     * @return - Returns list of item category.
     */
    List<String> getItemCategory();


    /**
     * Fetch list of items based on the given conditions
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDirection
     * @param itemName
     * @param itemCategories
     * @return Returns list of item category
     */
    Page<ItemDTO> getItems(int pageNo, int pageSize, String sortBy, String sortDirection, String itemName, List<String> itemCategories);

    /**
     * Fetch All Item Name
     *
     * @return - Returns list of item name.
     */
    List<String> getAllItemName();

    ItemDTO findItemById(Long id);

    ItemDTO getItemByName(String itemName);
}
